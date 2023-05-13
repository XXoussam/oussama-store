package com.fsb.oussama_store.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class ItemDTO {
    private Long id;
    private String name;
    private String imgUrl;
    private float price;
    private String description;
}
