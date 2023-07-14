package coin;

import coin.persistence.Round;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class FlipperController {

    private final FlipperService flipperService;
    private final RoundService roundService;

    record RoundDraft(
            String player,
            BigDecimal amount,
            Round.Outcome outcome
    ) {}

    @PostMapping("/api/rounds")
    public Round makeRound(@RequestBody RoundDraft roundDraft) {
        return this.flipperService.makeRound(roundDraft.player, roundDraft.amount, roundDraft.outcome);
    }

    @GetMapping("/api/rounds")
    public Collection<Round> getRounds() {
        return this.roundService.getActiveRounds();
    }

    record JoinRequest(
            Long roundId,
            String player,
            Round.Outcome outcome
    ) {}

    @PostMapping("/api/rounds/join")
    public void joinRound(@RequestBody JoinRequest joinRequest) {
        this.flipperService.joinRound(joinRequest.roundId, joinRequest.player, joinRequest.outcome);
    }
}
