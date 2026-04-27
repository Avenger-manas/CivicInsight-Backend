package dolpi.CivicInsight.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dolpi.CivicInsight.Entity.GroqAnalysis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class GroqService {
    @Value("${groq.api.key:${GROQ_API_KEY:}}")
    private String apiKey;

    @Value("${groq.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String apiUrl;

    private final WebClient webClient = WebClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();
    public GroqAnalysis analyzeComplaint(String complaintText) {

        String systemPrompt = """
            You are a Municipal Complaint Analysis System. Analyze complaints and return ONLY a JSON response.
            DEPARTMENTS & THEIR KEYWORDS:
            1. Municipal Department    → garbage, waste, sewage, drainage, dirty roads, water supply, pipe leak, pothole, street cleaning
            2. Animal Control Board    → stray dogs, animal bite, dead animal, monkey, snake, rabies, stray cats
            3. Education Department    → school, teacher absent, mid-day meal, classroom, student, fees, principal
            4. Electricity Department → power cut, street light, sparking wire, electric shock, meter, transformer, no electricity
            URGENCY RULES (read carefully):
            HIGH (Immediate Action Required within 24 hours):
            - Electric sparking, live wire on road, electric shock incident
            - Animal bite (dog, monkey, snake) — risk of rabies or venom
            - Dead animal on road causing disease/smell spreading
            - Sewage overflow entering homes or drinking water
            - Flooding due to blocked drains causing property damage
            - Teacher or staff physically harassing a student
            - Complete electricity failure in hospital or emergency area
            MEDIUM (Action Required within 3-5 days):
            - Garbage not collected for more than 3 days
            - Street lights not working for multiple nights
            - Stray dogs roaming in colony but no bite yet
            - School teacher absent repeatedly without reason
            - Water supply disrupted for more than 2 days
            - Pothole causing minor accidents or traffic issues
            - Transformer making noise or flickering lights
            LOW (Action Required within 2 weeks):
            - First time garbage collection missed
            - General inquiry about municipal services
            - Suggestion or feedback
            - Street light off for 1 night only
            - Minor pothole with no accident reported
            - Request for new dustbin or street light installation
            OUTPUT (return ONLY this JSON, no extra text, no markdown):
            {
              "Department": "<Municipal Department | Animal Control Board | Education Department | Electricity Department>",
              "Urgency": "<HIGH | MEDIUM | LOW>",
              "Category": "<2-4 word issue label>",
              "Summary": "<one sentence describing the complaint>",
              "Suggested_Action": "<one sentence on what should be done>"
            }
            RULES:
            - Output ONLY the JSON object
            - No markdown, no explanation, no code blocks
            - Choose department strictly from the 4 options above
            - Base analysis only on what is written, never assume
            - When in doubt between HIGH and MEDIUM, choose HIGH for safety
            """;

        String userPrompt = """
            Analyze this complaint and return ONLY the JSON:
            COMPLAINT:
            %s
            IMPORTANT: Return ONLY the JSON object. No other text.
            """.formatted(complaintText);

        Map<String, Object> requestBody = Map.of(
                "model", "llama-3.1-8b-instant",
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),  // ← system alag
                        Map.of("role", "user", "content", userPrompt)        // ← user alag
                ),
                "temperature", 0.3  // ← kam rakho taaki consistent JSON aaye
        );

        try {
            Map response = webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            List<Map> choices = (List<Map>) response.get("choices");
            Map message = (Map) choices.get(0).get("message");
            String rawText = (String) message.get("content");

            // Clean aur parse karo
            String clean = rawText
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            int start = clean.indexOf("{");
            int end = clean.lastIndexOf("}") + 1;
            if (start != -1 && end > start) {
                clean = clean.substring(start, end);
            }

            return objectMapper.readValue(clean, GroqAnalysis.class);

        } catch (Exception e) {
            log.error("Groq complaint analysis failed: {}", e.getMessage());
            // Fallback
            return GroqAnalysis.builder()
                    .Department("Municipal Department")
                    .Urgency("MEDIUM")
                    .Category("General Complaint")
                    .Summary("Complaint submitted by citizen.")
                    .Suggested_Action("Please review and assign to appropriate officer.")
                    .build();
        }
    }
}
