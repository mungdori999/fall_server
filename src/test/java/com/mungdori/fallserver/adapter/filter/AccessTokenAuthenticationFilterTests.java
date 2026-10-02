package com.mungdori.fallserver.adapter.filter;

import com.mungdori.fallserver.adapter.security.JwtTokenProvider;
import com.mungdori.fallserver.adapter.security.AdminOnly;
import com.mungdori.fallserver.adapter.webapi.AdminApi;
import com.mungdori.fallserver.domain.admin.AdminRegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import jakarta.servlet.FilterChain;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class AccessTokenAuthenticationFilterTests {
    @Test void registrationPassesWithoutToken() throws Exception {
        var tokens = mock(JwtTokenProvider.class);
        var filter = new AccessTokenAuthenticationFilter(tokens, "http://localhost:5173");
        var request = new MockHttpServletRequest("POST", "/api/admin");
        var response = new MockHttpServletResponse();
        var chain = mock(FilterChain.class);
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
        verifyNoInteractions(tokens);
        assertThat(AdminApi.class.getMethod("registerAdmin", AdminRegisterRequest.class)
                .isAnnotationPresent(AdminOnly.class)).isFalse();
        assertThat(AdminApi.class.isAnnotationPresent(AdminOnly.class)).isFalse();
    }

    @Test void otherAdminRequestsStillRequireToken() throws Exception {
        var filter = new AccessTokenAuthenticationFilter(mock(JwtTokenProvider.class), "http://localhost:5173");
        for (String[] endpoint : new String[][]{{"GET", "/api/admin"}, {"POST", "/api/admin/member"},
                {"GET", "/api/admin/member/list"}, {"DELETE", "/api/admin"}, {"POST", "/api/admin/other"}}) {
            var request = new MockHttpServletRequest(endpoint[0], endpoint[1]);
            var response = new MockHttpServletResponse();
            var chain = mock(FilterChain.class);
            filter.doFilter(request, response, chain);
            assertThat(response.getStatus()).isEqualTo(401);
            verifyNoInteractions(chain);
        }
    }
}
