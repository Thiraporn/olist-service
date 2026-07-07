package com.ana.olist.dashboards.sale.repository

import com.ana.olist.dashboards.sale.DTOs.MonthlyRevenueProjection
import com.ana.olist.dashboards.sale.DTOs.MonthlyRevenueSummaryProjection
import com.ana.olist.entities.StgOrder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
@Repository
interface RevenueRepository : JpaRepository<StgOrder, String> {
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
    fun findMonthlyRevenueReport(): List<MonthlyRevenueProjection>


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
    fun findMonthlyRevenueSummary(): List<MonthlyRevenueSummaryProjection>

}
