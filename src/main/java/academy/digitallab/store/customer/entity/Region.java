package academy.digitallab.store.customer.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name="Region")
@Table(name="tbl_regions")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
