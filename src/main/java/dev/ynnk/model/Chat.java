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
    private String id;


    @ManyToOne(fetch = FetchType.EAGER)
    private User personA;

    @ManyToOne(fetch = FetchType.EAGER)
    private User personB;


}
