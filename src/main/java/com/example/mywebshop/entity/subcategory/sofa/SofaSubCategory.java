package com.example.mywebshop.entity.subcategory.sofa;

import com.example.mywebshop.entity.Product;
import com.example.mywebshop.entity.subcategory.ColorProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SofaSubCategory {

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "external_material_id", referencedColumnName = "fk__external_material_id")
    private ExternalMaterialSofa externalMaterial;

    @ManyToOne
    @JoinColumn(name = "internal_material_id", referencedColumnName = "fk__internal_material_id")
    private InternalMaterialSofa internalMaterial;

    @ManyToOne
    @JoinColumn(name = "transformation_mechanism_id", referencedColumnName = "fk__transformation_mechanism_id")
    private TransformationMechanismSofa transformationMechanism;

    @ManyToOne
    @JoinColumn(name = "external_material_id", referencedColumnName = "fk__external_material_id")
    private ColorProperty color;

    private Integer placeCount;
}
