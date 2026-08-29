package com.ana.olist.dashboards.customer.repository

import com.ana.olist.dashboards.customer.projection.CustomerLifetimeValueProjection
import com.ana.olist.dashboards.customer.projection.CustomersContributeProjection
import com.ana.olist.dashboards.customer.projection.HighestRankingCustomerCityProjection
import com.ana.olist.dashboards.customer.projection.MadePurchasesCustomersProjection
import com.ana.olist.dashboards.customer.projection.RepeatCustomerProjection
import com.ana.olist.entities.StgOrder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface  CustomerAnalyticsRepository : JpaRepository<StgOrder, String> {


   //6️. How many unique customers have made purchases on the platform?  ลูกค้าทั้งหมดมีกี่คน หรือ จำนวนลูกค้าทั้งหมดที่เคยสั่งซื้อสินค้า (นับลูกค้าแต่ละคนแค่ครั้งเดียว)
    @Query(
        nativeQuery = true,
        value = """
             with  _made_purchases_customers as (
                select  count(distinct so.customer_id  )  made_purchases_customers
                from stg_orders so  where order_approved_at is not null
                ) ,_all_customers as (
                select count(   customer_id  )  all_customers  from stg_customers sc
                )
                select *
                from _made_purchases_customers
                cross join  _all_customers
            """
    )
   fun findMadePurchasesCustomersDashboard(): List<MadePurchasesCustomersProjection>




    //7️. What percentage of customers are repeat customers?  ลูกค้าซื้อซ้ำ (Repeat Customers) มีกี่เปอร์เซ็นต์
    /* Repeat Customer Rate (%) = (จำนวนลูกค้าที่ซื้อซ้ำ / จำนวนลูกค้าทั้งหมด) × 100  */
    @Query(
        nativeQuery = true,
        value = """ with   _repeat_customer as(
                select count(*)  repeat_customer
                from( select so.customer_id  ,count(so.order_id  ) count_orders
                from stg_orders so
                group by so.customer_id
                having count(*) > 1
                ) rp
                ),_made_purchases_customers as (
                select count(distinct so.customer_id  )  made_purchases_customers
                from stg_orders so  where order_approved_at is not null
                )
                select * ,(COALESCE( repeat_customer,0)/COALESCE( made_purchases_customers,1)) * 100   repeat_customer_rate
                from _repeat_customer
                cross join _made_purchases_customers 
                """
    )
   fun findRepeatCustomerDashboard(): List<RepeatCustomerProjection>




   //8️. What is the average spending per customer (Customer Lifetime Value)?  ลูกค้าใช้เงินเฉลี่ยต่อคนเท่าไร (Customer Lifetime Value)   ---> ลูกค้าหนึ่งคนสร้างรายได้ให้บริษัททั้งหมดเท่าไร
    /*   sum by customer ---> avg ค่าใช้จ่ายเฉลี่ยต่อลูกค้า 1 คน  */
   @Query(
       nativeQuery = true,
       value = """  with _customer_lift_value as(
                    select so.customer_id  ,count(distinct so.order_id  )  purchase_frequency
                    , MIN(order_approved_at) first_order_place
                    , MAX(order_approved_at) lasted_order_place
                    , SUM(soi.price +soi.freight_value )  CLV
                    from stg_orders so
                    join stg_order_items soi on  so.order_id  = soi.order_id
                    where order_approved_at is not null
                    group by so.customer_id
                    )
                    select AVG(CLV ) avg_spending from _customer_lift_value
                    """
    )
   fun findCustomerLifetimeValueDashboard(): List<CustomerLifetimeValueProjection>



    //9️. Which cities or states have the highest number of customers? เมืองหรือรัฐไหนมีลูกค้ามากที่สุด
    @Query(
        nativeQuery = true,
        value = """ 
            with _ranking_customer_city as (
            select sc.customer_city
            , count(distinct so.customer_id )  count_purchased_customers
            , count(distinct sc.customer_id )  count_registered_customers
            , dense_rank() over( order by count(distinct so.customer_id ) desc)  rank
            from stg_orders so
            right join stg_customers sc  on  so.customer_id  = sc.customer_id
            group by sc.customer_city
        
            )
            select * from _ranking_customer_city where  rank = 1
            """
    )
   fun findHighestRankingCustomerCityDashboard(): List<HighestRankingCustomerCityProjection>




   //10.Do the top 20% of customers generate the majority of the revenue (Pareto analysis)?  ลูกค้ากลุ่มไหนสร้างรายได้มากที่สุด เช่น Top 20% customers generate how much revenue?
    /*เพื่อยิ่ง ad,promotion ให้ลค กลุ่มนี้*/
   //Result  53.32788604218788   ---> Top 20% of customers generate 53.3% of total revenue.
    /*
     * Interpretation: Revenue กระจุกตัวอยู่ในลูกค้าบางส่วน แต่ ยังไม่ถึง 80/20 rule แบบ classic Pareto
     *                 (Revenue concentration ปานกลาง Customer base กระจายพอสมควร Business risk ไม่พึ่งลูกค้ากลุ่มเล็กมากเกินไป(ดูจาก 20% → 40–55% revenue เสี่ยงต่ำ))
     *
     *
     *
     *
     * Revenue is moderately concentrated among high-value customers.
      The top 20% of customers contribute 53.3% of total revenue,
      indicating that while high-spending customers are important,
      the revenue base is still relatively diversified.
      ลูกค้ากลุ่มบนมีบทบาทสำคัญ แต่รายได้ยังไม่ได้พึ่งลูกค้ากลุ่มเล็กมากเกินไป -->กลุ่ม 20%
      */
   @Query(
       nativeQuery = true,
       value = """ 
            with  total_revenue_by_customer as(
            select   SUM(soi.price +soi.freight_value )  _total_revenue_by_customer
            ----prevent same total c1 200    c2 200   ====> _rank c1 1,c2 2
            ,row_number() over(order by  SUM(soi.price +soi.freight_value )  desc) _rank
            from stg_orders so
            join stg_order_items soi on  so.order_id  = soi.order_id
            where order_approved_at is not null
            group by so.customer_id
            )
            , total_revenue as (
            select sum(_total_revenue_by_customer) total_revenue
            ,count(*) count_customer
            , cast(count(*)*0.2 as INT) _20percent
            from total_revenue_by_customer
            ) , _20percent_segment as (
            select *
            from total_revenue_by_customer
            cross join total_revenue
            where _rank <= _20percent
            )
            --find Revenue share
            select sum(_total_revenue_by_customer)*100 /total_revenue.total_revenue cal_revenue_share
            from _20percent_segment
            cross join total_revenue
            group by  total_revenue.total_revenue
            """
    )
   fun findCustomersContributeDashboard(): List<CustomersContributeProjection>


}