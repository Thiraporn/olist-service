package com.ana.olist.dashboards.delivery.repository

import com.ana.olist.dashboards.delivery.projection.AverageDeliveryProjection
import com.ana.olist.dashboards.delivery.projection.CustomerRegionDeliveredProjection
import com.ana.olist.dashboards.delivery.projection.SellerDeliveryProjection
import com.ana.olist.dashboards.delivery.projection.EstimatedDeliveryPercentageProjection
import com.ana.olist.dashboards.delivery.projection.TotalFreightPerOrderProjection
import com.ana.olist.entities.StgOrder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface  DeliveryAnalyticsRepository : JpaRepository<StgOrder, String> {


    /*11️. What is the average delivery time from purchase to delivery? ระยะเวลาจัดส่งเฉลี่ยกี่วัน*/
     @Query(
        nativeQuery = true,
        value = """
             with find_delivered as (
                 select 
                    -- carrier -> customer (วันจริง)
                    ROUND(
                        EXTRACT(EPOCH FROM (
                            so.order_delivered_customer_date - so.order_delivered_carrier_date
                        )) / 86400,
                        2
                    ) AS logistics_delivery_days,
                    -- carrier -> customer (ชั่วโมงจริง)
                    ROUND(
                        EXTRACT(EPOCH FROM (
                            so.order_delivered_customer_date - so.order_delivered_carrier_date
                        )) / 3600,
                        2
                    ) AS logistics_delivery_hours,
                    -- purchase -> customer (วันจริง)   23:59 → 00:01 = 2 นาที  ที่ไม่ควรถูกนับเป็น 1 วัน  
                    ROUND(
                        EXTRACT(EPOCH FROM (
                            so.order_delivered_customer_date - so.order_approved_at
                        )) / 86400,
                        2
                    ) AS purchase_delivery_days, 
                    -- Delivery Status
                    CASE
                        WHEN so.order_delivered_customer_date <= so.order_estimated_delivery_date::timestamp
                        THEN 'On Time'
                        ELSE 'Delay'
                    END AS status_of_delivery,
                     -- Delay (วันจริง, ติดลบ = ส่งก่อนกำหนด)
                    ROUND(
                        EXTRACT(EPOCH FROM (
                            so.order_delivered_customer_date - so.order_estimated_delivery_date::timestamp
                        )) / 86400,
                        2
                    ) AS delay_delivery,
                     * 
                     from stg_orders so 
                     where  order_approved_at is not null 
                     and so.order_delivered_customer_date is not null
                     and so.order_status in ('delivered')
                     )
                    select     ROUND(AVG(purchase_delivery_days), 2) AS avg_days_of_delivery
                    from find_delivered
            """
    )
   fun findAverageDeliveryDashboard(): List<AverageDeliveryProjection>

    /*12️. What percentage of orders are delivered later than the estimated delivery date? เปอร์เซ็นต์การส่งล่าช้า (Late Delivery Rate) สูตร late_delivery = delivered_date > estimated_delivery_date
       *  A late delivery rate of 6.77% suggests that the majority of orders meet the
          promised delivery timeline, indicating efficient logistics operations and
          accurate delivery estimates.
       *
       *
       * */
    @Query(
        nativeQuery = true,
        value = """  
                with find_delivered as (
                 select 
                  -- carrier -> customer (วันจริง)
                    ROUND(
                        EXTRACT(EPOCH FROM (
                            so.order_delivered_customer_date - so.order_delivered_carrier_date
                        )) / 86400,
                        2
                    ) AS logistics_delivery_days,
                  -- carrier -> customer (ชั่วโมงจริง)
                    ROUND(
                        EXTRACT(EPOCH FROM (
                            so.order_delivered_customer_date - so.order_delivered_carrier_date
                        )) / 3600,
                        2
                    ) AS logistics_delivery_hours,
                  
                  -- purchase -> customer (วันจริง)   23:59 → 00:01 = 2 นาที  ที่ไม่ควรถูกนับเป็น 1 วัน  
                    ROUND(
                        EXTRACT(EPOCH FROM (
                            so.order_delivered_customer_date - so.order_approved_at
                        )) / 86400,
                        2
                    ) AS purchase_delivery_days, 
                 -- Delivery Status
                    CASE
                        WHEN so.order_delivered_customer_date <= so.order_estimated_delivery_date::timestamp
                        THEN 'On Time'
                        ELSE 'Delay'
                    END AS status_of_delivery,
                     -- Delay (วันจริง, ติดลบ = ส่งก่อนกำหนด)
                    ROUND(
                        EXTRACT(EPOCH FROM (
                            so.order_delivered_customer_date - so.order_estimated_delivery_date::timestamp
                        )) / 86400,
                        2
                    ) AS delay_delivery,
                    * 
                 from stg_orders so 
                 where  order_approved_at is not null 
                 and so.order_delivered_customer_date is not null
                 and so.order_status in ('delivered')
                 )
                select count(status_of_delivery ) count_all_delivery  
                ,count(CASE WHEN status_of_delivery = 'Delay' THEN 1 END) AS   count_delay  
                , round(count(CASE WHEN status_of_delivery = 'Delay' THEN 1 END)*100.0 /  count(status_of_delivery ) ,2) percent_of_delay
                , round(count(CASE WHEN status_of_delivery != 'Delay' THEN 1 END)*100.0/  count(status_of_delivery ) ,2) percent_of_ontime
                 
                from find_delivered
                """
    )
   fun findEstimatedDeliveryPercentageDashboard(): List<EstimatedDeliveryPercentageProjection>

 /*13️. Which sellers have the fastest average delivery times? seller ไหนส่งของเร็วที่สุด
  ปัญหา data ที่พบ
  * 1. A small number of records contain inconsistent timestamps where the
     carrier pickup date occurs after the customer delivery date,
     indicating potential data quality issues.
  * ====> show comment in : imposible case so.order_delivered_carrier_date >  so.order_delivered_customer_date
  *
  *
  * 2. in order to prevent small sample bias  ---> HAVING COUNT(*) >= 10
  *
  *
  * Insight :
         Sellers were ranked based on their average delivery time
         from carrier pickup to customer delivery.

         To ensure statistical reliability, only sellers with at least
         20 delivered orders were included in the analysis.
  *
  *
  *
  */
   @Query(
       nativeQuery = true,
       value = """   
              with sellers_delivered as (
                 select 
                  --carrier -> customer
                  ROUND(
                        EXTRACT(EPOCH FROM (
                            so.order_delivered_customer_date - so.order_delivered_carrier_date
                        )) / 86400,
                        2
                    ) AS logistics_delivery_days, 
                  seller_id ,so.*
                 from stg_orders so 
                 join stg_order_items soi on  so.order_id  = soi.order_id 
                 where  order_approved_at is not null 
                 and so.order_delivered_customer_date is not null
                 and so.order_status in ('delivered')
                 --เจอปัญหา data เพราะลูกค้าไม่สามารถได้รับของก่อน carrier รับของ    อาจจะเป็นเพราะว่า timestamp  , timezone mismatch,   ETL error ,data entry error
                 --imposible case so.order_delivered_carrier_date >  so.order_delivered_customer_date 
                 and so.order_delivered_carrier_date <=  so.order_delivered_customer_date  
                 )
                select seller_id,COUNT(*) AS total_orders,ROUND(AVG(logistics_delivery_days), 2) avg_seller_delivered
                from sellers_delivered
                group by    seller_id
                --prevent small sample bias
                HAVING COUNT(*) >= 10
                order by    avg_seller_delivered        
             """
    )
   fun findSellerDeliveryDashboard(): List<SellerDeliveryProjection>


    /*14. Which regions experience the longest delivery times?  รัฐไหนมี delivery time ช้าที่สุด
      *
      *  Delivery performance varies significantly across regions.
         The states such as RR, AM, and AP experience the longest
         delivery times, averaging over 20 days.

         In contrast, highly urbanized states such as MG, PR, and SP
         show significantly faster deliveries, averaging below 10 days.

      */
    @Query(
        nativeQuery = true,
        value = """ 
             with customer_region_delivered as (
                 select 
                  --carrier -> customer
                   ROUND(
                    EXTRACT(EPOCH FROM (
                        so.order_delivered_customer_date - so.order_delivered_carrier_date
                    )) / 86400,
                    2
                ) AS logistics_delivery_days, 
                   so.*,sc.customer_state 
                 from stg_orders so  
                 join stg_customers sc   on so.customer_id = sc.customer_id 
                 where  order_approved_at is not null 
                 and so.order_delivered_customer_date is not null
                 and so.order_status in ('delivered')
                 --เจอปัญหา data เพราะลูกค้าไม่สามารถได้รับของก่อน carrier รับของ    อาจจะเป็นเพราะว่า timestamp  , timezone mismatch,   ETL error ,data entry error
                 --imposible case so.order_delivered_carrier_date >  so.order_delivered_customer_date 
                 and so.order_delivered_carrier_date <=  so.order_delivered_customer_date 
             
             )
            select customer_state , COUNT(*) AS total_orders,ROUND(AVG(logistics_delivery_days), 2)  avg_customer_delivered
            from customer_region_delivered crd 
            group by customer_state 
            --prevent small sample bias
            --HAVING COUNT(*) >= 10 
            order by avg_customer_delivered desc;
            """
    )
   fun findCustomerRegionDeliveredDashboard(): List<CustomerRegionDeliveredProjection>

    /*15️. What is the average shipping cost (freight value) per order? shipping cost (freight_value) เฉลี่ยเท่าไรต่อ order*/
   @Query(
       nativeQuery = true,
       value = """ 
           with find_total_freight_per_order as(
              select SUM(soi.freight_value ) total_freight_per_order 
               from stg_orders so 
               join stg_order_items soi on  so.order_id  = soi.order_id 
               where order_approved_at is not null  
               group by soi.order_id 
           )
           select AVG(total_freight_per_order)  avg_freight
           from find_total_freight_per_order
            """
    )
   fun findTotalFreightPerOrderDashboard(): List<TotalFreightPerOrderProjection>


}