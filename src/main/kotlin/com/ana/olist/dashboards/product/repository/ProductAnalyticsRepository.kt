package com.ana.olist.dashboards.product.repository

import com.ana.olist.dashboards.product.projection.AveragePriceOfProductProjection
import com.ana.olist.entities.StgOrder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface  ProductAnalyticsRepository : JpaRepository<StgOrder, String> {

    /*19️. What is the average price of products in each category? ราคาเฉลี่ยของสินค้าในแต่ละหมวด*/
     @Query(
        nativeQuery = true,
        value = """
              select coalesce( n.product_category_name_english,'N/A') product_category_name_english ,ROUND(AVG(soi.price)::numeric ,2)  avg_price  ,COUNT(*) total_items_sold 
              from  stg_orders so  
              join  stg_order_items soi on  so.order_id  = soi.order_id 
              join stg_products sp on soi.product_id = sp.product_id  
              full outer join stg_product_category_name_translation n  on   sp.product_category_name = n.product_category_name
              where  order_approved_at is not null 
              group by n.product_category_name_english  
            """
    )
   fun findAveragePriceOfProductDashboard(): List<AveragePriceOfProductProjection>










}