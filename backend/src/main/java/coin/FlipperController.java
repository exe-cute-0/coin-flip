package coin;

import coin.persistence.Round;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class FlipperController {

    private final FlipperService flipperService;
    private final RoundService roundService;

    record RoundDraft(
            String player,
            Round.Outcome outcome
    ) {}

    @PostMapping("/rounds")
    public Round makeRound(@RequestBody RoundDraft roundDraft) {
        return this.flipperService.makeRound(roundDraft.player, roundDraft.outcome);
    }

    @GetMapping("/rounds")
    public Collection<Round> getRounds() {
        return this.roundService.getActiveRounds();
    }

    record JoinRequest(
            Long roundId,
            String player,
            Round.Outcome outcome
    ) {}

    @PostMapping("/rounds/join")
    public void joinRound(@RequestBody JoinRequest joinRequest) {
        this.flipperService.joinRound(joinRequest.roundId, joinRequest.player, joinRequest.outcome);
    }
}
