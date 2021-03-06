package it.chalmers.gamma.api;

import static it.chalmers.gamma.utils.CharacterTypes.LOWERCASE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import it.chalmers.gamma.GammaApplication;
import it.chalmers.gamma.domain.dto.access.ITClientDTO;
import it.chalmers.gamma.endoints.JSONParameter;
import it.chalmers.gamma.factories.MockITClientFactory;
import it.chalmers.gamma.utils.GenerationUtils;
import it.chalmers.gamma.utils.JSONUtils;
import java.util.Objects;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = GammaApplication.class)
@ActiveProfiles("test")
@SuppressWarnings({"PMD.JUnitTestsShouldIncludeAssert", "PMD.ExcessiveImports"})
public class OauthTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private MockITClientFactory mockITClientFactory;

    private static final String TEST_COM = "https://test.com/auth";

    @Before
    public void setupTests() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    /**
     * Tests if going to oauth endpoints as non-authenticated redirects to login page.
     * @throws Exception if mockMvc cannot perform
     */
    @Test
    public void testLoginRedirect() throws Exception {
        this.mockMvc.perform(
               get("/oauth")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
        .andDo(print());
    }

    // This will fail right now.
    @WithMockUser
    @Test
    public void testAuthorizationRequestWithIncorrectId() throws Exception {
        ITClientDTO clientDTO = this.mockITClientFactory
                .saveClient(this.mockITClientFactory.generateClient(TEST_COM));

        String query = getTestAuthorizationQuery(GenerationUtils.generateRandomString(40, LOWERCASE),
                clientDTO.getWebServerRedirectUri());
        this.mockMvc.perform(get(query))
                .andExpect(status().is(422))
                .andExpect(status().reason("NO_SUCH_CLIENT_EXISTS"));
    }

    @WithMockUser
    @Test
    public void testAuthorizationRequestWithIncorrectRedirect() throws Exception {
        ITClientDTO clientDTO = this.mockITClientFactory
                .saveClient(this.mockITClientFactory.generateClient(TEST_COM));

        String query = getTestAuthorizationQuery(clientDTO.getClientId(),
                GenerationUtils.generateRandomString(40, LOWERCASE));
        this.mockMvc.perform(get(query)).andExpect(status().is(400));
    }

    private String getTestAuthorizationQuery(String clientId, String redirect) {
        return String.format("/oauth/authorize/?%s", JSONUtils.toFormUrlEncoded(
                new JSONParameter("client_id", clientId),
                new JSONParameter("redirect_uri", redirect),
                new JSONParameter("response_type", "code"))
        );
    }

    @WithMockUser
    @Test
    public void testSuccessfulAuthorizationRequest() throws Exception {
        String redirect = TEST_COM;
        ITClientDTO clientDTO = this.mockITClientFactory
                .saveClient(this.mockITClientFactory
                        .generateClient(redirect));
        String query = getTestAuthorizationQuery(clientDTO.getClientId(), clientDTO.getWebServerRedirectUri());
        this.mockMvc.perform(get(query)).andExpect(redirectedUrlPattern(String.format("%s?code=**", redirect)));
    }

    @WithMockUser
    @Test
    public void testSuccessfulAuthorizationCode() throws Exception {
        String redirect = TEST_COM;
        ITClientDTO clientDTO = this.mockITClientFactory
                .saveClient(this.mockITClientFactory
                        .generateClient(redirect));
        String query = getTestAuthorizationQuery(clientDTO.getClientId(), clientDTO.getWebServerRedirectUri());
        MvcResult result = this.mockMvc.perform(get(query)).andDo(print()).andReturn();
        String code = Objects.requireNonNull(result.getResponse().getRedirectedUrl()).split("code=")[1];
        String tokenQuery = JSONUtils.toFormUrlEncoded(

                new JSONParameter("client_id", clientDTO.getClientId()),
                new JSONParameter("client_secret", clientDTO.getClientSecret().replace("{noop}", "")),
                new JSONParameter("code", code),
                new JSONParameter("grant_type", "authorization_code"),
                new JSONParameter("redirect_uri", clientDTO.getWebServerRedirectUri())
        );
        String rawAuth = clientDTO.getClientId() + ":"
                + clientDTO.getClientSecret().replace("{noop}", "");
        String auth = Base64.encodeBase64String(rawAuth.getBytes());
        this.mockMvc.perform(post("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Basic " + auth)
                .content(tokenQuery)).andDo(print()).andExpect(status().is(200));
    }
}
