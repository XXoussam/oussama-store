package com.fsb.oussama_store.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class PurchaseProcessDTO {
    private Long id;
    private ClientDTO client;
    private ItemDTO item;
    private Integer quantity;
}
