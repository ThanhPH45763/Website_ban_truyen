package com.example.backend_comic_service.develop.repository;

import com.example.backend_comic_service.develop.entity.OrderEntity;
import com.example.backend_comic_service.develop.model.mapper.OrderGetListMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    @Query(value = "select MAX(id) from [dbo].[orders]", nativeQuery = true)
    Integer getIdGenerateCode();
    @Query(value = "update orders set [status] = ?1 where id = ?2", nativeQuery = true)
    void updateOrderStatus(Integer status, Integer orderId);
    @Query(value = "    select\n" +
            "        od.id as orderId,\n" +
            "        od.code as orderCode,\n" +
            "        FORMAT(od.order_date, 'dd/MM/yyyy') as orderDate,\n" +
            "        cast(od.total_price as int) as totalPrice,\n" +
            "        od.status as orderStatus,\n" +
            "        od.stage as orderStage,\n" +
            "        cast(od.fee_delivery as int) as feeDelivery,\n" +
            "        od.payment_id as paymentId,\n" +
            "        p.[name] as paymentName,\n" +
            "        od.user_id as userId,\n" +
            "        u.full_name as customerName,\n" +
            "        u.phone_number as phoneNumber,\n" +
            "        od.employee_id as employeeId,\n" +
            "        emp.full_name as employeeName,\n" +
            "        od.type as orderType,\n" +
            "         CONCAT(ad.address_detail, ' - ', ward.name, ' - ', distr.name, ' - ', province.name) as addressDetail,\n" +
            "        d.[name] as deliveryName,\n" +
            "        od.delivery_type as deliveryType,\n" +
            "        od.address_id as addressId         \n" +
            "    from\n" +
            "        orders od        \n" +
            "    left outer join\n" +
            "        users u              \n" +
            "            on od.[user_id] = u.id              \n" +
            "    left outer join\n" +
            "        payments p              \n" +
            "            on od.payment_id = p.id       \n" +
            "    left outer join\n" +
            "        users emp              \n" +
            "            on od.employee_id = emp.id       \n" +
            "    left outer join\n" +
            "        [address] ad              \n" +
            "            on od.address_id = ad.id       \n" +
            "    left outer join\n" +
            "        delivery d              \n" +
            "            on od.delivery_type = d.id \n" +
            "    left outer join provinces province\n" +
            "\t        on province.code = ad.province_id\n" +
            "    left outer join districts distr\n" +
            "\t        on distr.code = ad.district_id\n" +
            "    left outer join wards ward\n" +
            "\t        on ward.code = ad.ward_id\n" +
            " where od.[user_id] = isnull(?1, od.[user_id])\n" +
            "      and od.payment_id = isnull(?2, od.payment_id)\n" +
            "  and od.employee_id = isnull(?3, od.employee_id)\n" +
            "  and od.[status] = isnull(?4, od.[status])\n" +
            "  and od.stage = isnull(?5, od.stage)\n" +
            "  and od.[type] = isnull(?6, od.[type])\n" +
            "  and od.total_price between isnull(?7, od.total_price) and isnull(?8, od.total_price)\n" +
            "  and od.order_date between isnull(?9, od.order_date) and isnull(?10, od.order_date)", nativeQuery = true)
    Page<OrderGetListMapper> getListOrder(Integer userId, Integer paymentId, Integer employeeId, Integer status, Integer stage, Integer type, Integer startPrice, Integer endPrice, Date startDate, Date endDate, Pageable pageable);
}