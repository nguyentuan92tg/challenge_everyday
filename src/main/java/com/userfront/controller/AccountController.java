package com.userfront.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.PrimaryTransaction;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.SavingsTransaction;
import com.userfront.domain.User;
import com.userfront.service.AccountService;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {

  @Autowired
  private UserService userService;

  @Autowired
  private AccountService accountService;

  @Autowired
  private TransactionService transactionService;

  @GetMapping("/primaryAccount")
  public String primaryAccount(Model model, Principal principal) {
    List<PrimaryTransaction> primaryTransactionList = transactionService
        .findPrimaryTransactionList(principal.getName());
    User user = userService.findByUsername(principal.getName());
    PrimaryAccount primaryAccount = user.getPrimaryAccount();
    model.addAttribute("primaryAccount", primaryAccount);
    model.addAttribute("primaryTransactionList", primaryTransactionList);
    return "primaryAccount";
  }

  @GetMapping("/savingsAccount")
  public String savingsAccount(Model model, Principal principal) {
    List<SavingsTransaction> savingsTransactionList = transactionService
        .findSavingsTransactionList(principal.getName());
    User user = userService.findByUsername(principal.getName());
    SavingsAccount savingsAccount = user.getSavingsAccount();

    model.addAttribute("savingsAccount", savingsAccount);
    model.addAttribute("savingsTransactionList", savingsTransactionList);

    return "savingsAccount";
  }

  @GetMapping(value = "/deposit")
  public String deposit(Model model) {
    model.addAttribute("accountType", "");
    model.addAttribute("amount", "");
    return "deposit";
  }

  @PostMapping(value = "/deposit")
  public String depositPOST(@ModelAttribute("amount") String amount,
      @ModelAttribute("accountType") String accountType, Principal principal) {
    accountService.deposit(accountType, Double.parseDouble(amount), principal);
    return "redirect:/userFront";
  }

  @GetMapping(value = "/withdraw")
  public String withdraw(Model model) {
    model.addAttribute("accountType", "");
    model.addAttribute("amount", "");
    return "withdraw";
  }

  @PostMapping(value = "/withdraw")
  public String withdrawPOST(@ModelAttribute("amount") String amount,
      @ModelAttribute("accountType") String accountType, Principal principal) {
    accountService.withdraw(accountType, Double.parseDouble(amount), principal);
    return "redirect:/userFront";
  }
}
