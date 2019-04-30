package com.community.rest.controller;

import com.community.rest.domain.Member;
import com.community.rest.domain.item.Item;
import com.community.rest.service.ItemService;
import com.community.rest.service.MemberService;
import com.community.rest.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	MemberService memberService;

	@Autowired
	ItemService itemService;

	@RequestMapping(value="/order", method = RequestMethod.GET)
	public String createForm(Model model) {
		List<Member> members = memberService.findMembers();
		List<Item> items = itemService.findItems();

		model.addAttribute("members", members);
		model.addAttribute("items", items);
		return "order/orderFrom";
	}

	@RequestMapping(value="/order", method = RequestMethod.POST)
	public String order(@RequestParam("memberId") Long memberId,
	                    @RequestParam("ItemId") Long itemId,
	                    @RequestParam("count") int count) {
		orderService.order(memberId, itemId, count);
		return "redirect:/orders";
	}

}
