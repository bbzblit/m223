package dev.ynnk.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Chat {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    private Person personA;

    @ManyToOne(fetch = FetchType.EAGER)
    private Person personB;

}
