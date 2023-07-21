package com.example.test.webrtc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MainService {
    private static final String REDIRECT="redirect:/";
    private final RoomService roomService;
    private final Parser parser;

    @Autowired
    public MainService(RoomService roomService, Parser parser) {
        this.roomService = roomService;
        this.parser = parser;
    }

    /* Main 화면 반환 */
    public ModelAndView displayMainPage(final Long id, final String uuid) {
        final ModelAndView modelAndView=new ModelAndView("main");
        modelAndView.addObject("id",id);
        modelAndView.addObject("rooms",roomService.getRooms());
        modelAndView.addObject("uuid",uuid);

        return modelAndView;
    }

    /*
    방 만들기 요청
    방 만들어진 Main 화면 반환
     */
    public ModelAndView processRoomSelection(final String sid, final String uuid, final BindingResult binding) {
        if(binding.hasErrors()){
            // simplified version, no errors processing
            return new ModelAndView(REDIRECT);
        }
        Optional<Long> optionId=parser.parseId(sid);
        optionId.ifPresent(id->Optional.ofNullable(uuid).ifPresent(name->roomService.addRoom(new Room(id))));

        return this.displayMainPage(optionId.orElse(null),uuid);
    }

    /*
    Main 화면에서 방 클릭 시 실행
    chat_room.html 반환
     */
    public ModelAndView displaySelectedRoom(final String sid, final String uuid) {
        // redirect to main page if provided data is invalid
        //System.out.println("SERVICE: BEFORE MODELANDVIEW");
        ModelAndView modelAndView=new ModelAndView(REDIRECT);
        //System.out.println("SERVICE: AFTER MODELANDVIEW");
        if(parser.parseId(sid).isPresent()){
            //System.out.println("SERVICE: PARSING SID IS PRESENT");
            Room room=roomService.findRoomByStringId(sid).orElse(null);
            System.out.println("SERVICE: "+room.toString());
            if(room!=null&&uuid!=null&&!uuid.isEmpty()){
                modelAndView=new ModelAndView("chat_room","id",sid);
                modelAndView.addObject("uuid",uuid);
            }
        }

        return modelAndView;
    }

    /*
    방에서 나갈 때 실행
    Main 화면 반환
     */
    public ModelAndView processRoomExit(String sid, String uuid) {
        if(sid!=null&&uuid!=null){
            System.out.println("User "+uuid+" has left Room #"+sid);
            // implement any logic you need
        }
        return new ModelAndView(REDIRECT);
    }

    /*
    방번호 랜덤 요청 시 발생
    생성 후 Main 화면 반환
     */
    public ModelAndView requestRandomRoomNumber(final String uuid) {
        return  this.displayMainPage(randomValue(),uuid);
    }

    private Long randomValue(){
        return ThreadLocalRandom.current().nextLong(0,100);
    }
}
