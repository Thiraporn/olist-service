package com.ana.olist.dashboards.sale.repository

import com.ana.olist.dashboards.sale.projection.AverageOrderValueProjection
import com.ana.olist.dashboards.sale.projection.HighestRevenueByProductCategoriesProjection
import com.ana.olist.dashboards.sale.projection.MonthlyRevenueProjection
import com.ana.olist.dashboards.sale.projection.MonthlyRevenueSummaryProjection
import com.ana.olist.dashboards.sale.projection.Top10BestSellingProductsProjection
import com.ana.olist.entities.StgOrder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
@Repository
interface RevenueAnalyticsRepository : JpaRepository<StgOrder, String> {
    //1. What is the total revenue generated each month?   รายได้รวมของบริษัทต่อเดือนเป็นเท่าไร
    /*Remarks : Revenue => full payment_value ,order_approved_at => Revenue is recognized when payment is approved.
               payment_installments > 1   ===> start from order_approved_at     sum_rev/payment_installments  in order_approved_at ?
    */
    @Query(
        value = """
        WITH total_revenue_by_month AS (
            SELECT
                EXTRACT(YEAR FROM order_approved_at)::int AS year,
                EXTRACT(MONTH FROM order_approved_at)::int AS month,
                SUM(soi.price + soi.freight_value)::numeric AS total_revenue
            FROM stg_orders so
            JOIN stg_order_items soi
                ON so.order_id = soi.order_id
            WHERE order_approved_at IS NOT NULL
            GROUP BY
                EXTRACT(YEAR FROM order_approved_at),
                EXTRACT(MONTH FROM order_approved_at)
        ),
        current_prev_total_revenue AS (
            SELECT *,
                   LAG(total_revenue) OVER (
                       ORDER BY year, month
                   ) AS prev_month_revenue
            FROM total_revenue_by_month
        )
        SELECT
            year,
            month,
            ROUND(total_revenue, 4) AS current_total_revenue,
            ROUND(COALESCE(prev_month_revenue,0),4) AS prev_month_revenue,
            ROUND(
                CASE
                    WHEN prev_month_revenue > 0
                    THEN ((total_revenue - prev_month_revenue)
                          / prev_month_revenue) * 100
                    ELSE 0
                END,
                2
            ) AS mom_growth
        FROM current_prev_total_revenue
        ORDER BY year, month
        """,
        nativeQuery = true
    )
    fun findMonthlyRevenueDashboard(): List<MonthlyRevenueProjection>

    //2️. How does monthly revenue change over time (Month-over-Month growth)?  ยอดขายเติบโตหรือหดตัวเทียบกับเดือนก่อน (MoM Growth) ช่วยดูว่า ธุรกิจกำลังโตหรือกำลังชะลอ ดู trend โต หรือ ไม่โต
    /* MoM Growth (%) = (Current Month Value − Previous Month Value) / Previous Month Value × 100  */
    @Query(
        value = """
        SELECT
            EXTRACT(YEAR FROM order_approved_at)::int AS year,
            EXTRACT(MONTH FROM order_approved_at)::int AS month,
            ROUND(SUM(soi.price + soi.freight_value)::numeric,4) AS totalRevenue
        FROM stg_orders so
        JOIN stg_order_items soi
            ON so.order_id = soi.order_id
        WHERE order_approved_at IS NOT NULL
        GROUP BY
            EXTRACT(YEAR FROM order_approved_at),
            EXTRACT(MONTH FROM order_approved_at)
        ORDER BY
            year,
            month
    """,
        nativeQuery = true
    )
    fun findMonthlyRevenueSummaryDashboard(): List<MonthlyRevenueSummaryProjection>


    //3. Which product categories generate the highest revenue?  หมวดสินค้าที่สร้างรายได้สูงสุดคืออะไร
    @Query(
        value = """
         WITH revenue_by_product_cat AS(
           select  product_category_name_english,ROUND(SUM(soi.price +soi.freight_value )::numeric ,2) as total_revenue 
           from stg_orders so 
           join stg_order_items soi on  so.order_id  = soi.order_id 
           join stg_products sp on soi.product_id = sp.product_id 
           join stg_product_category_name_translation n  on   sp.product_category_name = n.product_category_name
           where order_approved_at is not null
           group by product_category_name_english
	   
         ) ,revenue_by_product_cat_sort as (
             select * , rank() over(order by total_revenue desc)   rank_total_revenue
             from revenue_by_product_cat  
         )	 
          select * from revenue_by_product_cat_sort where rank_total_revenue = 1; 
    """, nativeQuery = true
    )
    fun findHighestRevenueByProductCategoriesDashboard(): List<HighestRevenueByProductCategoriesProjection>

    //4. What are the top 10 best-selling products by number of orders?  สินค้า 10 รายการที่ถูกสั่งซื้อบ่อยที่สุด (จำนวน order มากที่สุด)
    @Query(
        value = """
             with revenue_by_orderid as(
               select   soi.product_id, product_category_name_english 
               ,count(distinct so.order_id)  count_order
               ,rank() over(order by count(distinct so.order_id) desc)  rank_count_order_id
               from stg_orders so 
               join stg_order_items soi on  so.order_id  = soi.order_id 
               join stg_products sp on soi.product_id = sp.product_id 
               join stg_product_category_name_translation n  on   sp.product_category_name = n.product_category_name
               --where order_approved_at is not null
               group by soi.product_id, product_category_name_english
               
         )  
          select * from revenue_by_orderid  where  rank_count_order_id < 11 
    """, nativeQuery = true
    )
    fun findTop10BestSellingProductsDashboard(): List<Top10BestSellingProductsProjection>


    //5. What is the Average Order Value (AOV)?      Average Order Value (AOV)  ของร้านค้าเท่าไร    สูตร AOV = total_revenue / total_orders
    /*---> ลูกค้าโดยเฉลี่ยใช้เงินเท่าไรต่อ order, มูลค่าเฉลี่ยของเงินที่ลูกค้าใช้ต่อ 1 คำสั่งซื้อ
     *---> ลูกค้าใช้เงินต่อครั้งมากแค่ไหน
     *---> ประสิทธิภาพของโปรโมชั่น
     *
     **/
    @Query(
        value = """
             select   SUM(soi.price +soi.freight_value ) total_revenue
               , count(distinct soi.order_id ) total_orders  
               , SUM(soi.price +soi.freight_value )/count(distinct soi.order_id ) AOV
               from stg_orders so 
               join stg_order_items soi on  so.order_id  = soi.order_id 
               where order_approved_at is not null ;
    """, nativeQuery = true
    )
    fun findAverageOrderValueDashboard(): List<AverageOrderValueProjection>

}
