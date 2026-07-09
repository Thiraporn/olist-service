package com.ana.olist.dashboards.seller.repository

import com.ana.olist.dashboards.seller.projection.AverageShippingCostByReviewScoreProjection
import com.ana.olist.dashboards.seller.projection.CorrelationProjection
import com.ana.olist.dashboards.seller.projection.HighestReviewScoresProjection
import com.ana.olist.dashboards.seller.projection.LowestSatisfactionCategoriesProjection
import com.ana.olist.dashboards.seller.projection.SellersRevenueProjection
import com.ana.olist.entities.StgOrder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface  SellerAnalyticsRepository : JpaRepository<StgOrder, String> {

    /*16️. Which sellers generate the highest total revenue? seller คนไหนสร้างรายได้มากที่สุด*/
     @Query(
        nativeQuery = true,
        value = """
             with sellers_revenue as (
                 select  soi.seller_id 
                 ,ROUND( SUM(soi.price +soi.freight_value)::numeric, 2) sellers_revenue
                 from stg_orders so 
                 join stg_order_items soi on  so.order_id  = soi.order_id 
                 where  order_approved_at is not null  
                 group by soi.seller_id 
             )
             select * from sellers_revenue  sn
             where sellers_revenue = ( select MAX(sellers_revenue) highest_sellers_revenue from sellers_revenue _max   )
            """
    )
   fun findSellersRevenueDashboard(): List<SellersRevenueProjection>



    /*17️. Which sellers receive the highest average customer review scores? seller คนไหนมี rating สูงที่สุด

         *
         * Seller performance was evaluated using average customer
           review scores. To avoid bias from small sample sizes,
           only sellers with at least 50 reviews were included.
         *
         */
    @Query(
        nativeQuery = true,
        value = """  
                select seller_id ,count(sor.review_id ) count_reviews, ROUND(AVG(sor.review_score)::numeric,2) review_score_by_seller
                  from stg_order_reviews sor 
                  left join stg_orders so  on sor.order_id = so.order_id 
                  left join (select distinct order_id,seller_id from stg_order_items   ) soi on  so.order_id  = soi.order_id 
                  where  order_approved_at is not null and seller_id is not null
                  group by seller_id
                  --prevent small sample bias
                  having count(sor.review_id ) > 50
                  order by review_score_by_seller desc
                """
    )
   fun findHighestReviewScoresDashboard(): List<HighestReviewScoresProjection>




    /*18. Which product categories have the lowest customer satisfaction scores? หมวดสินค้าที่ได้รับ review ต่ำที่สุดคืออะไร*/
   @Query(
       nativeQuery = true,
       value = """   
             select  n.product_category_name_english   ,count(sor.review_id ) count_reviews, ROUND(AVG(sor.review_score)::numeric,2)  review_score_by_product_cate
              from stg_order_reviews sor 
              left join stg_orders so  on sor.order_id = so.order_id 
              left join (select distinct order_id,product_id  
                         from stg_order_items   ) soi on  so.order_id  = soi.order_id 
              join stg_products sp on soi.product_id = sp.product_id 
              join stg_product_category_name_translation n  on   sp.product_category_name = n.product_category_name
              where  order_approved_at is not null 
              group by  n.product_category_name_english  
             
              --prevent small sample bias
              --having count(sor.review_id ) > 50
              order by review_score_by_product_cate asc
              limit 1       
             """
    )
   fun findLowestSatisfactionCategoriesDashboard(): List<LowestSatisfactionCategoriesProjection>

    /*20. Is there a relationship between shipping cost and customer review scores? freight_value มีผลต่อ review score หรือไม่
          20.1 KPI วัดความสัมพันธ์เชิงสถิติ
             * Shipping cost has a very weak negative correlation with customer review scores (r = -0.089).
             * This suggests that shipping cost alone is not a major factor influencing customer satisfaction.
             * ค่าขนส่งมีความสัมพันธ์เชิงลบในระดับอ่อนมากกับคะแนนรีวิว (r = -0.089) แสดงว่าค่าขนส่งเพียงอย่างเดียวไม่น่าจะเป็นปัจจัยหลักที่ส่งผลต่อความพึงพอใจของลูกค้า
             * */
    /*
     *
     * แปลว่า แทบไม่มีความสัมพันธ์ ระหว่าง ค่าขนส่ง (Freight Cost) กับ คะแนนรีวิว (Review Score)
            การตีความโดยทั่วไปคือ

            Correlation (r)	ความหมาย
            0	ไม่มีความสัมพันธ์
            ±0.00 – ±0.19	อ่อนมาก (Very Weak)
            ±0.20 – ±0.39	อ่อน (Weak)
            ±0.40 – ±0.59	ปานกลาง (Moderate)
            ±0.60 – ±0.79	สูง (Strong)
            ±0.80 – ±1.00	สูงมาก (Very Strong)
     *
     * --- Correlation = -0.089
     *
     */

    @Query(
        nativeQuery = true,
        value = """ 
            WITH freight_per_order AS (
                SELECT
                    order_id,
                    SUM(freight_value) AS total_freight
                FROM stg_order_items
                GROUP BY order_id
            )
            
            SELECT
                CORR(total_freight, review_score) AS correlation
            FROM freight_per_order fp
            JOIN stg_order_reviews sor
                ON fp.order_id = sor.order_id;
            """
    )
   fun findCorrelationDashboard(): List<CorrelationProjection>




    /*20.2 Chart Is there a relationship between shipping cost and customer review scores?   ดูแนวโน้ม (Descriptive Analysis)
     *
     * KPI() 20.1 : Correlation = -0.089
       Chart 20.2 : Average Shipping Cost by Review Score
       Insight :
           Average shipping cost decreases slightly as review scores increase.
           However, the Pearson correlation coefficient is -0.089, indicating a very weak negative relationship.
           This suggests that shipping cost alone is unlikely to be a major driver of customer review scores.

     *
     *
     */
   @Query(
       nativeQuery = true,
       value = """ 
           WITH freight_per_order AS (
                SELECT
                    order_id,
                    SUM(freight_value)::numeric AS total_freight
                FROM stg_order_items
                GROUP BY order_id
            )
            SELECT
                sor.review_score,
                COUNT(*) AS total_orders,
                ROUND(AVG(fp.total_freight), 2) AS avg_shipping_cost
            FROM freight_per_order fp
            JOIN stg_order_reviews sor
                ON fp.order_id = sor.order_id
            GROUP BY sor.review_score
            ORDER BY sor.review_score;
            """
    )
   fun findAverageShippingCostByReviewScoreDashboard(): List<AverageShippingCostByReviewScoreProjection>


}