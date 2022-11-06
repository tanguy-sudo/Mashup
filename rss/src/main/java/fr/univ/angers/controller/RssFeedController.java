package fr.univ.angers.controller;

import fr.univ.angers.view.RssFeedView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

@RestController
@RequestMapping("/rss")
public class RssFeedController {

    @Autowired
    private RssFeedView view;

    @GetMapping("/lastDay")
    public View getFeed() {
        return view;
    }
}
