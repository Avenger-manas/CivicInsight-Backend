package dolpi.CivicInsight.Entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
public class OllamaResponse {
    @JsonUnwrapped
    private ComplaintAnalysis analysis;
}
