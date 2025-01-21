package com.wan.commonservice.domain.dto;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.function.Supplier;

@Data
@ApiModel(value = "分页对象")
// 当设置为 chain = true 时，Lombok 会生成返回当前对象本身的 setter 方法（即返回类型为 this），
//  而不是返回 void。这使得你可以链式调用多个 setter 方法。
@Accessors(chain = true)
public class PageDTO {
    public static final Integer DEFAULT_PAGE_SIZE = 20;
    public static final Integer DEFAULT_PAGE_NUM = 1;
    @ApiModelProperty(value = "分页页码")
    @Min(value = 1, message = "分页大小必须大于0")
    private Integer pageNum = DEFAULT_PAGE_NUM;
    @ApiModelProperty(value = "分页大小")
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    @ApiModelProperty("是否升序")
    private Boolean isAsc = true;
    @ApiModelProperty("排序方式")
    private String sortBy;

    /**
     * 将当前配置转换为分页对象，用于数据库查询时的分页和排序设置
     * 此方法主要用于创建一个分页对象，并根据提供的默认排序字段和排序方向进行排序设置
     * 如果当前配置中没有指定排序字段，则使用提供的默认排序字段和排序方向
     *
     * @param defaultSortBy 默认排序字段，当未指定排序字段时使用
     * @param isAsc 是否升序，true表示升序，false表示降序
     * @return 返回一个设置了分页和排序信息的Page对象
     */
    public <T> Page<T> toMapPage(String defaultSortBy, boolean isAsc) {
        // 如果当前配置中的排序字段为空，则使用提供的默认排序字段和排序方向
        if (StringUtils.isBlank(sortBy)) {
            sortBy = defaultSortBy;
            this.isAsc = isAsc;
        }
        // 创建一个分页对象，传入当前配置的页码和页面大小
        Page<T> page = new Page<>(pageNum, pageSize);
        // 创建一个排序项对象，用于设置排序信息
        OrderItem orderItem = new OrderItem();
        // 设置排序方向
        orderItem.setAsc(isAsc);
        // 设置排序字段
        orderItem.setColumn(sortBy);
        // 将排序信息添加到分页对象中
        page.addOrder(orderItem);
        // 返回设置了分页和排序信息的分页对象
        return page;
    }

    public <T> Page<T> toMapPageDefaultSortByCreateTimeDesc() {
        return toMapPage("create_time", false);
    }

    public <T> Page<T> toMapPageDefaultSortByCreateTime(boolean isAsc) {
        return toMapPage("create_time", isAsc);
    }
}
