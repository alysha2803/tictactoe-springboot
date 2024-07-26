package com.game.tictactoe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class IndexController {

    private String[] cells = {"", "", "", "", "", "", "", "", ""}; 
    private String winner = null; 

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("showTable", false);
        model.addAttribute("cells", cells);
        model.addAttribute("winner", winner);
        return "index";
    }

    @PostMapping("/submit")
    public String submitForm(@RequestParam("fname") String name, Model model) {
        model.addAttribute("showTable", true);
        model.addAttribute("cells", cells);
        model.addAttribute("winner", winner);
        return "index";
    }

    @PostMapping("/makeMove")
    public String makeMove(@RequestParam("choice") int choice, Model model) {
        String errorMessage = null;

        if (choice < 1 || choice > 9) {
            errorMessage = "Invalid choice. Enter a number between 1 and 9.";
        } else if (!cells[choice - 1].isEmpty()) {
            errorMessage = "Occupied";
        } else {
            cells[choice - 1] = "X";
            if (checkWinner("X")) {
                winner = "PLAYER WINS";
                model.addAttribute("winner", winner);
                model.addAttribute("showTable", true);
                model.addAttribute("cells", cells);
                model.addAttribute("errorMessage", errorMessage);
                return "index";
            }

            List<Integer> availablePositions = new ArrayList<>();
            for (int i = 0; i < cells.length; i++) {
                if (cells[i].isEmpty() && (i + 1) != choice) {
                    availablePositions.add(i);
                }
            }

            if (!availablePositions.isEmpty()) {
                Collections.shuffle(availablePositions); 
                int randomIndex = availablePositions.get(0); 
                cells[randomIndex] = "O";
                if (checkWinner("O")) {
                    winner = "CPU WINS";
                    model.addAttribute("winner", winner);
                    model.addAttribute("showTable", true);
                    model.addAttribute("cells", cells);
                    model.addAttribute("errorMessage", errorMessage);
                    return "index";
                }
            }
        }

        model.addAttribute("showTable", true);
        model.addAttribute("cells", cells);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("winner", winner); 
        return "index";
    }

    private boolean checkWinner(String player) {
        int[][] winningConditions = {
            {0, 1, 2}, 
            {3, 4, 5}, 
            {6, 7, 8}, 
            {0, 3, 6}, 
            {1, 4, 7}, 
            {2, 5, 8}, 
            {0, 4, 8}, 
            {2, 4, 6} 
        };

        for (int[] condition : winningConditions) {
            if (cells[condition[0]].equals(player) &&
                cells[condition[1]].equals(player) &&
                cells[condition[2]].equals(player)) {
                return true;
            }
        }

        return false;
    }
}
