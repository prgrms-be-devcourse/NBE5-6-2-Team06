package com.grepp.matnam.app.controller.web.team;

import com.grepp.matnam.app.model.team.ParticipantRepository;
import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.app.model.team.code.ParticipantStatus;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.app.model.user.UserService;
import com.grepp.matnam.app.model.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TeamControllerTest {
    @Mock
    private TeamService teamService;

    @Mock
    private UserService userService;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Model model;

    @InjectMocks
    private TeamController teamController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(teamController).build();
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("로그인하지 않은 사용자도 모임 상세 페이지에 접근할 수 있어야 함")
    void anonymousUserCanAccessTeamDetail() throws Exception {
        Authentication anonymousAuth = new AnonymousAuthenticationToken(
                "anonymous", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
        );

        when(securityContext.getAuthentication()).thenReturn(anonymousAuth);

        Team mockTeam = new Team();
        mockTeam.setTeamId(1L);
        mockTeam.setTeamTitle("테스트 모임");
        mockTeam.setParticipants(new ArrayList<>());

        when(teamService.getTeamByIdWithParticipants(anyLong())).thenReturn(mockTeam);

        String viewName = teamController.teamDetail(1L, model);

        verify(model).addAttribute(eq("team"), eq(mockTeam));
        verify(model).addAttribute(eq("participants"), anyList());
        verify(model).addAttribute(eq("isAnonymous"), eq(true));
        verify(model).addAttribute(eq("isLeader"), eq(false));
        verify(model).addAttribute(eq("isParticipant"), eq(false));
        verify(model).addAttribute(eq("alreadyApplied"), eq(false));

        verify(userService, never()).getUserById(anyString());

        assert viewName.equals("team/teamDetail");
    }

    @Test
    @DisplayName("로그인한 사용자는 모임 상세 페이지에서 추가 정보를 볼 수 있어야 함")
    void authenticatedUserCanAccessTeamDetailWithFullInfo() throws Exception {
        String userId = "testUser";
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userId, "password",
                AuthorityUtils.createAuthorityList("ROLE_USER")
        );

        when(securityContext.getAuthentication()).thenReturn(auth);

        User mockUser = new User();
        mockUser.setUserId(userId);
        mockUser.setNickname("테스트 사용자");

        Team mockTeam = new Team();
        mockTeam.setTeamId(1L);
        mockTeam.setTeamTitle("테스트 모임");
        mockUser.setUserId("otherUser");
        mockTeam.setUser(mockUser);
        mockTeam.setParticipants(new ArrayList<>());

        when(teamService.getTeamByIdWithParticipants(anyLong())).thenReturn(mockTeam);
        when(userService.getUserById(userId)).thenReturn(mockUser);
        when(participantRepository.existsByUser_UserIdAndTeam_TeamIdAndParticipantStatus(
                eq(userId), anyLong(), eq(ParticipantStatus.APPROVED))).thenReturn(false);
        when(participantRepository.existsByUser_UserIdAndTeam_TeamIdAndParticipantStatus(
                eq(userId), anyLong(), eq(ParticipantStatus.PENDING))).thenReturn(false);

        String viewName = teamController.teamDetail(1L, model);

        verify(model).addAttribute(eq("team"), eq(mockTeam));
        verify(model).addAttribute(eq("participants"), anyList());
        verify(model).addAttribute(eq("isAnonymous"), eq(false));
        verify(model).addAttribute(eq("user"), eq(mockUser));
        verify(model).addAttribute(eq("isLeader"), anyBoolean());
        verify(model).addAttribute(eq("isParticipant"), anyBoolean());
        verify(model).addAttribute(eq("alreadyApplied"), anyBoolean());

        verify(userService, times(1)).getUserById(userId);

        assert viewName.equals("team/teamDetail");
    }

    @Test
    @DisplayName("익명 사용자 접근 통합 테스트")
    void anonymousUserCanAccessTeamDetailMvcTest() throws Exception {
        Authentication anonymousAuth = new AnonymousAuthenticationToken(
                "anonymous", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
        );

        when(securityContext.getAuthentication()).thenReturn(anonymousAuth);

        Team mockTeam = new Team();
        mockTeam.setTeamId(1L);
        mockTeam.setTeamTitle("테스트 모임");
        mockTeam.setParticipants(new ArrayList<>());

        when(teamService.getTeamByIdWithParticipants(anyLong())).thenReturn(mockTeam);

        mockMvc.perform(get("/team/detail/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("team/teamDetail"))
                .andExpect(model().attributeExists("team"))
                .andExpect(model().attribute("isAnonymous", true))
                .andExpect(model().attribute("isLeader", false))
                .andExpect(model().attribute("isParticipant", false))
                .andExpect(model().attribute("alreadyApplied", false));

        verify(userService, never()).getUserById(anyString());
    }
}