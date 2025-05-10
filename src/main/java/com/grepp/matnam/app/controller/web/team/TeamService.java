package com.grepp.matnam.app.controller.web.team;

import com.grepp.matnam.app.model.team.entity.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public String uploadImage(MultipartFile image) {

        return "image_url_placeholder";
    }

    public void saveTeam(Team team) {
        teamRepository.save(team);
    }

    @Value("${google.api.key}")
    private String googleApiKey;

}
