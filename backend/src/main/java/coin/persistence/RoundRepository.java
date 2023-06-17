package coin.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface RoundRepository extends JpaRepository<Round, Long> {

    List<Round> findByFlippedIsNullOrFlippedAfter(OffsetDateTime threshold);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE round SET player_heads = :player WHERE round_id = :roundId AND player_heads IS NULL")
    int joinHeads(@Param("roundId") Long roundId, @Param("player") String player);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE round SET player_tails = :player WHERE round_id = :roundId AND player_tails IS NULL")
    int joinTails(@Param("roundId") Long roundId, @Param("player") String player);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE round SET outcome = :outcome, flipped = :flipped WHERE round_id = :roundId")
    int setOutcome(
            @Param("roundId") Long roundId,
            @Param("outcome") Character outcome,
            @Param("flipped") OffsetDateTime flipped);
}
