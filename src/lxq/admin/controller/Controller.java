package lxq.admin.controller;

import java.util.ArrayList;
import java.util.List;

import lxq.user.util.FormString;

import com.base.BaseController;
import com.bean.LotteryLog;
import com.bean.OpenNum;
import com.bean.TaskTimerBean;
import com.bean.TimeNumOver;
import com.config.ControllerBind;
import com.jfinal.aop.Clear;

import demo.UserInterceptor;

@ControllerBind(controllerKey = "/home")
@Clear(UserInterceptor.class)
public class Controller extends BaseController{
	
	public void index(){
		//获取倒计时
		TaskTimerBean taskt = TaskTimerBean.dao.findById(1);
		if(taskt.getInt("status")==1){ //如果开奖器是开启的则获取数据库中的倒计时时间
			TimeNumOver tlong = TimeNumOver.dao.findById(1);
			int timeNum = tlong.getInt("number");
			setAttr("tlong",timeNum*1000);
		}else{
			setAttr("tlong",-1);
		}
		
		//获取开奖记录
		LotteryLog Llog = LotteryLog.dao.findFirst("SELECT * FROM lottery_log ORDER BY creantime DESC");
		FormString fstring = new FormString();
		Llog.put("firstNum", fstring.firstNum(Llog.getInt("Num")+""));
		Llog.put("secondNum", fstring.secondNum(Llog.getInt("Num")+""));
		Llog.put("threeNum", fstring.threeNum(Llog.getInt("Num")+""));
		setAttr("Llog",Llog);
		
		//获取每日开奖的期数
		OpenNum ON = OpenNum.dao.findById(1);
		ON.put("nextTime",getYearMd()+fstring.formNum(ON.getInt("openNum"),ON.getInt("nowNum")));
		setAttr("ON",ON);
		renderAuto("/home.html");
	}
	
	public void resHtml(){
		List<LotteryLog> Llog = LotteryLog.dao.find("SELECT * FROM lottery_log ORDER BY creantime DESC LIMIT 85");
		LotteryLog LlogCh = LotteryLog.dao.findFirst("SELECT * FROM lottery_log ORDER BY creantime DESC");
		FormString fstring = new FormString();
		LlogCh.put("firstNum", fstring.firstNum(LlogCh.getInt("Num")+""));
		LlogCh.put("secondNum", fstring.secondNum(LlogCh.getInt("Num")+""));
		LlogCh.put("threeNum", fstring.threeNum(LlogCh.getInt("Num")+""));
		setAttr("Llog",LlogCh);
		
		setAttr("dateList",Llog);
		renderAuto("/index.html");
	}
	
	public void overres(){
		FormString fstring = new FormString();
		List<LotteryLog> Llog = LotteryLog.dao.find("SELECT * FROM lottery_log ORDER BY creantime DESC LIMIT 85");
		List<LotteryLog> chList = new ArrayList<LotteryLog>();
		for(LotteryLog chLlog : Llog){
			LotteryLog lolog = new LotteryLog();
			lolog.put("creantime", chLlog.getStr("creantime"));
			lolog.put("qiNum", chLlog.getStr("qiNum"));
			lolog.put("firstNum", fstring.firstNum(chLlog.getInt("Num")+""));
			lolog.put("secondNum", fstring.secondNum(chLlog.getInt("Num")+""));
			lolog.put("threeNum", fstring.threeNum(chLlog.getInt("Num")+""));
			lolog.put("bigorsam", fstring.bigorsam(chLlog.getInt("Num")+""));
			lolog.put("onlyAll", fstring.onlyAll(chLlog.getInt("Num")+""));
			lolog.put("allNum", fstring.allNum(chLlog.getInt("Num")+""));
			chList.add(lolog);
		}
		setAttr("dateList",chList);
		renderAuto("/overres.html");
	}
	
	public void findRes(){
		System.out.println(getPara("key"));
		FormString fstring = new FormString();
		List<LotteryLog> Llog = LotteryLog.dao.find("SELECT * FROM lottery_log WHERE qiNum LIKE '%"+getPara("key")+"%' ORDER BY creantime DESC LIMIT 57");
		List<LotteryLog> chList = new ArrayList<LotteryLog>();
		for(LotteryLog chLlog : Llog){
			LotteryLog lolog = new LotteryLog();
			lolog.put("qiNum", chLlog.getStr("qiNum"));
			lolog.put("firstNum", fstring.firstNum(chLlog.getInt("Num")+""));
			lolog.put("secondNum", fstring.secondNum(chLlog.getInt("Num")+""));
			lolog.put("threeNum", fstring.threeNum(chLlog.getInt("Num")+""));
			lolog.put("bigorsam", fstring.bigorsam(chLlog.getInt("Num")+""));
			lolog.put("onlyAll", fstring.onlyAll(chLlog.getInt("Num")+""));
			lolog.put("allNum", fstring.allNum(chLlog.getInt("Num")+""));
			chList.add(lolog);
		}
		setAttr("dateList",chList);
		render("/computer/findDate.html");
	}
	
	//=========用户登陆===============
	public void login(){
		if("ksAdmin".equals(getPara("userName"))&&"awenjiusan@20158965;;!$".equals(getPara("password"))){
			setSessionAttr("loginUser", "ksAdmin");
			redirect("/info.html");
		}else{
			render("/computer/login.html");
		}
	}
	
	public void loginout(){
		removeSessionAttr("loginUser");
		render("/computer/login.html");
	}
	
	private class Invalid{
        
    }
}
