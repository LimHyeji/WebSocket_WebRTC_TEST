package com.example.test.webrtc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ControllerAdvice
public class MainController {
    private final MainService mainService;

    @Autowired
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    /* HOME 접속 시 실행 */
    @GetMapping({"","/","/index","/home"})
    public ModelAndView displayMainPage(final Long id,final String uuid){
        return this.mainService.displayMainPage(id,uuid);
    }

    /*
    room 생성 시 실행
    @param sid
    @param uuid
    @param binding
    @return
     */
    @PostMapping(value="/room",params = "action=create")
    public ModelAndView processRoomSelection(@ModelAttribute("id") final String sid, @ModelAttribute("uuid") final String uuid, final BindingResult binding){
        return this.mainService.processRoomSelection(sid,uuid,binding);
    }

    /*
    채팅방 입장 시 실행
    (채팅방 id, 입장 uuid 필요)
     */
    @GetMapping("/room/{sid}/user/{uuid}")
    public ModelAndView displaySelectedRoom(@PathVariable("sid") final String sid, @PathVariable("uuid") final String uuid){
        return this.mainService.displaySelectedRoom(sid,uuid);
    }

    /* 채팅방에서 나갈 때 실행 */
    @GetMapping("/room/{sid}/user/{uuid}/exit")
    public ModelAndView processRoomExit(@PathVariable("sid") final String sid,@PathVariable("uuid") final String uuid){
        return this.mainService.processRoomExit(sid,uuid);
    }

    @GetMapping("/room/random")
    public ModelAndView requestRandomRoomNumber(@ModelAttribute("uuid") final String uuid){
        return mainService.requestRandomRoomNumber(uuid);
    }

    /*
    client에서 sdp offer할 때 실행되며
    sdp_offer.html 반환함
     */
    @GetMapping("/offer")
    public ModelAndView displaySampleSdpOffer(){
        return new ModelAndView("sdp_offer");
    }

    @GetMapping("/stream")
    public ModelAndView displaySampleStreaming(){
        return new ModelAndView("streaming");
    }
}
