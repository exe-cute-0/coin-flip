package coin.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Arrays;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "round", schema = "flipper", indexes = {
        @Index(name = "idx_round_created", columnList = "created"),
        @Index(name = "idx_round_flipped", columnList = "flipped"),
})
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "round_id", columnDefinition = "int UNSIGNED not null")
    private Long id;

    @Column(name = "created", nullable = false)
    private OffsetDateTime created;

    @Column(name = "flipped")
    private OffsetDateTime flipped;

    @Column(name = "outcome")
    @Convert(converter = OutcomeConverter.class)
    private Outcome outcome;

    @Column(name = "player_heads", length = 20)
    private String playerHeads;

    @Column(name = "player_tails", length = 20)
    private String playerTails;

    @RequiredArgsConstructor
    public enum Outcome {
        HEADS('H'),
        TAILS('T');

        @Getter
        private final Character value;
    }

    static class OutcomeConverter implements AttributeConverter<Outcome, Character> {

        @Override
        public Character convertToDatabaseColumn(Outcome outcome) {
            if (outcome == null) return null;
            return outcome.value;
        }

        @Override
        public Outcome convertToEntityAttribute(Character character) {
            if (character == null) return null;
            return Arrays.stream(Outcome.values())
                    .filter(o -> o.value.equals(character))
                    .findAny()
                    .orElseThrow(() -> new IllegalStateException("No such outcome '%s'".formatted(character)));
        }
    }
}