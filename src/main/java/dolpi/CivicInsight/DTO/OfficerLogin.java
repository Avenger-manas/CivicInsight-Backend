package dolpi.CivicInsight.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfficerLogin {
    @NonNull
    private String username;

    @NonNull
    private String password;
}
