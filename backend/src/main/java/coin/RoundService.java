package coin;

import coin.persistence.Round;
import coin.persistence.RoundRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RoundService {

    private final EntityManager entityManager;
    private final RoundRepository repository;
    private final ConcurrentHashMap<Long, Round> activeRounds;

    public RoundService(
            EntityManager entityManager,
            RoundRepository repository
    ) {
        this.entityManager = entityManager;
        this.repository = repository;
        this.activeRounds = new ConcurrentHashMap<>();
    }

    @Scheduled(fixedDelay = 1, initialDelay = 2, timeUnit = TimeUnit.SECONDS)
    public void sync() {
        for (Round round : this.repository.findByFlippedIsNullOrFlippedAfter(OffsetDateTime.now().minusSeconds(10))) {
            this.entityManager.detach(round);
            this.activeRounds.put(round.getId(), round);
        }

        for (Round round : this.getActiveRounds()) {
            if (round.getFlipped() != null && OffsetDateTime.now().minusSeconds(10).isAfter(round.getFlipped())) {
                this.activeRounds.remove(round.getId());
            }
        }

        log.debug("Active rounds: {}", this.activeRounds.size());
    }

    public Collection<Round> getActiveRounds() {
        return this.activeRounds.values();
    }

    public Round createRound(Round round) {
        round.setCreated(OffsetDateTime.now());
        return this.repository.save(round);
    }

    public void joinRound(Long roundId, String player, Round.Outcome side) {
        int modifications = switch (side) {
            case HEADS -> this.repository.joinHeads(roundId, player);
            case TAILS -> this.repository.joinTails(roundId, player);
        };

        if (modifications != 1) {
            throw new RuntimeException("Unable to join round");
        }
    }

    public void concludeRound(Long roundId, Round.Outcome outcome) {
        int modifications = this.repository.setOutcome(roundId, outcome.getValue(), OffsetDateTime.now());

        if (modifications != 1) {
            throw new RuntimeException("Unable to set outcome");
        }
    }
}
