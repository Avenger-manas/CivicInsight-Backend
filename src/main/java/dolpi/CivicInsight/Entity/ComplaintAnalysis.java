package dolpi.CivicInsight.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintAnalysis {
    @JsonProperty("Category")
    private String category;

    @JsonProperty("Department")
    private String department;

    @JsonProperty("Suggested_Action")
    private String suggestedAction;

    @JsonProperty("Summary")
    private String summary;

    @JsonProperty("Urgency")
    private String urgency;
}
