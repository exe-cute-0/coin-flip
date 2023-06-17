package coin;

import coin.persistence.Round;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.random.RandomGenerator;

@Service
@RequiredArgsConstructor
public class FlipperService {

    private final RoundService roundService;
    private final RandomGenerator randomGenerator;

    public Round makeRound(String player, BigDecimal amount, Round.Outcome side) {
        Round.RoundBuilder draft = Round.builder()
                .amount(amount);

        if (side.equals(Round.Outcome.HEADS)) {
            draft.playerHeads(player);
        } else {
            draft.playerTails(player);
        }

        return this.roundService.createRound(draft.build());
    }

    @Transactional
    public void joinRound(Long roundId, String player, Round.Outcome side) {
        this.roundService.joinRound(roundId, player, side);

        Round.Outcome outcome;
        if (this.randomGenerator.nextBoolean()) {
            outcome = Round.Outcome.HEADS;
        } else {
            outcome = Round.Outcome.TAILS;
        }

        this.roundService.concludeRound(roundId, outcome);
    }
}
