package com.case_fullstack.mastermind.models.entities;

import com.case_fullstack.mastermind.models.enums.Colors;
import com.case_fullstack.mastermind.models.enums.MatchStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MatchStatus matchStatus;

    private LocalDateTime initialDate;
    private LocalDateTime finalDate;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Colors> correctAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "match", fetch =  FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Attempt> attempts = new ArrayList<>();
}
