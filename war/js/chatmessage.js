//@4:32331.21
/*
32=wrongName.sendRedirect.getRequestURI+200.getResponseHeader
33=sendRedirect.setHeader.errorResponse+401.getResponseHeader.error
1=error
room=location.href*2
wellcome=location.href*1
*/
function ChatManager($url,$rec){
//@0:res5
	//t:[res5]+return2.$userName+2111.2111.213111
/*
$rec[name].res=
res,$(uiContent),$(divData),$(content),$(userName).html(),name);
(!room || cached)
!$rec[name].res
*/
	var uiContent='.ui-page-active .ui-content',
		divData='.ui-page-active .divData',
		content='.ui-page-active .content',
		userName='#userName';
	var timer;
	this.getName=getName;
	this.getChatMessages=function(room){
		getChatMessages(room, function(){
/*
			clearInterval(timer);
			timer=interval(5000);
*/
			timer.interval(5000);
		});
	};
	function forMap(){
		var btn='#trivia-button',clr=true;
		for(var i in $rec){
			if(false){
			if($rec[i].res){
				tManager.pushMessage(i,rec[i].res[rec[i].res.length-1].message,rec[i].res[rec[i].res.length-1].date,0);
				clr=false;
			}
			else tManager.pushMessage(i,null,null,-1);
			}
			getChatMessages(i, function(i, rec){
				chain(i,rec);
			});
		}
		if(clr){
		}
	}
	function chain(i, rec){
		if($rec[i].res) tManager.pushMessage(i,rec[i].res[rec[i].res.length-1].message,rec[i].res[rec[i].res.length-1].date,0);
		else tManager.pushMessage(i,null,null,-1);
	}
	timer = new (function Timer(){
		var timer;
		this.interval=function(t,t1){
			clearInterval(timer);
			if(t1)setTimeout(function(){timer=interval(t);},t1);else timer=interval(t);
		};
	function interval(t){forMap();return setInterval(forMap,t);}
	})();
	function getName(){return decodeURIComponent($.mobile.path.parseLocation().search.replace('?room=',''));};
	this.sendMessage=function(msg){
		var name = getName();
		var thiz=this;
		if(msg)
			ajax($url,{action:'addMessage',name:name,message:msg},function(res){
				if($rec[name].res) getChatMessages(name,chain);
				if(res) thiz.getChatMessages(name);
			});
	}
//@2:234
	// 234=chain.chain+double.never.lag+norepeat.clear.insleep.test
	function getChatMessages(room,chain){
		var action = arguments.callee.name;
		var name = getName();
		var cached=room==name;
		if(room)name=room;
		if(!$rec[name])$rec[name]={};
		var date=$rec[name].date;
		if($rec[name].res && $rec[name].res.length) date=$rec[name].res[$rec[name].res.length-1].date;
		var async = (!$rec[name].res);
		if((!room || cached))
		$(divData).empty().html(name);
		if($rec[name].res && (!room || cached)){ //-1:有存&(本間+當間)
			console.log(1,room,name,$rec[name].res.length,$rec);
			if(wFocus.focus){
			$rec[name]=setChatMessage($rec[name].res,$(uiContent),$(divData),$(content),$(userName).html(),name);
			$rec[name].res=null;
			}
		}else if(!$rec[name].res) //-2:沒存
			ajax($url,{action:action,name:name,date:date},function(res){
				if(false) $rec[name]=setChatMessage(res,$(uiContent),$(divData),$(content),$(userName).html(),name);
				else if(!room || cached) $rec[name].res=res; //-2.1:(本間+當間)要存
				else $rec[name].res=res; //-2.2:別間要存
				console.log(2,room,name,res!=null?res.length:res,$rec);
				if (chain) chain(name, $rec);
				if ($rec[name].res) timer.interval(5000,1000);
			});
		else console.log(3,room,name,$rec[name].res.length,$rec); //-3:有存&別間
		if (!async && chain) chain(name, $rec);
		return name;
	}
	function setChatMessage(res,$uiContent,$divData,$content,$userName,name){
		if(res && $userName){
			var div1='<div style="font-size:12px"></div>',
				div2='<div style="display:inline-block;padding:8px;border-radius:10px;background-color:#99CCFF"></div>',
				span='<span></span>',
				span1='<span style="position:relative;top:5px;font-size:12px"></span>',
				p='<p></p>',
				p1='<p style="text-align:center;color:blue"></p>',
				p2='<p style="text-align:right"></p>',
				span2='<span class="last" style="display:none"></span>',
				last0=$content.children('p').last().children('.last').html(),
				cm0=encap(last0),
				d0;
			for(var i in res){
				var last1=res[i].name+'-'+res[i].date,
					cm1=encap(last1,res[i].message);
				if(!cm0 || cm1.d>cm0.d || (cm1.d==cm0.d && cm1.n != cm0.n)){
					if(!d0 || d0!=cm1.d1){
						d0=cm1.d1;
						if(!cm0 || d0>cm0.d1) $content.append($(p1).append($(span).html(d0)));
					}
					if($userName==cm1.n) $content.append($(p2).append(
						$(span2).html(last1)).append(
						$(span1).html(cm1.d2)).append(
						$(div2).append($(span).html(cm1.msg))));
					else $content.append($(p).append(
						$(span2).html(last1)).append(
						$(div1).html(cm1.n)).append(
						$(div2).append($(span).html(cm1.msg))).append(
						$(span1).html(cm1.d2))
					);
					cm0=cm1;
				}
			}
			$divData.empty().html(name);
			$uiContent.scrollTop($content.outerHeight());
		}
		return cm0?{name:cm0.n,date:cm0.d}:null;
	}
	function encap(last,msg){
		if(last){
			var d=last.substring(last.indexOf('-')+1),
				d1=d.substring(0,d.indexOf(' ')),
				d2=d.substring(0,d.indexOf('.')).replace(d1,'&nbsp;&nbsp;')+'&nbsp;&nbsp;';
			return {n:last.substring(0,last.indexOf('-')),d:d,d1:d1,d2:d2,msg:msg}
		}
		return null;
	}
	var tManager;
	this.setTManager=function(_tManager){tManager=_tManager;};
	var wFocus;
	this.setWFocus=function(_wFocus){wFocus=_wFocus;};
	this.getMsgInput=function(div,controlgroup,button){
		return new MsgInput(div,controlgroup,button,this);
	}
	function MsgInput(div,controlgroup,button,cManager){
		$('input',div).textinput({
		}).keypress(function(e){
			if(e.keyCode==13){
				cManager.sendMessage($(this).val());
				$(this).val('');
			}
		});
		this.resize=function(){
			var _left = $(controlgroup).position().left+$(controlgroup).outerWidth(),
				_right = $(button).position().left;
			$(div).css({position:'relative',left:_left}).outerWidth(_right-_left);
		}
	}
}
function triviaManager(id){
	var timer,map={},flash=new Flash();
	this.pushMessage=pushMessage;
	function pushMessage(name,msg,date,order){
		if(noRepeat(name,msg,date,order)){
			clearInterval(timer);
			if(order==0) flash.start(map[name].msg,name);
			else if (order==-1) flash.start(null,name,true);
			timer=interval(5000);
		}
	}
	function noRepeat(name,msg,date,order){
		if(!map[name])map[name]={};
		if(map[name].msg==msg && map[name].date==date && map[name].order==order)return false;
		map[name].msg=msg;
		map[name].date=date;
		map[name].order=order;
		return true;
	}
	function forMap(){
		if(flash.inSleep()){
			var name;
			for(var i in map)
				if(map[i].order==0)
					name=i;
			if(!name)
				for(var i in map)
					if(map[i].order==1)
						name=i;
			if(name) flash.start(map[name].msg,name);
		}
	}
	function interval(t){forMap();return setInterval(forMap,t);}
//@1:43.465
	/**
		43.465
		push.noRepeat+clearInterval.interval
		forMap.interval+inSleep
		
		flash=name.msg.timer.time
		var.start.console.clear.setTimeout.start
		if.if.clear.null.return
	*/
	function Flash(){
		var timer,time=0,msg,name; //f:a1=var4
		var msg0;
		function show(_id,_msg){$(_id).html(_msg);}
		function start(_msg,_name,_stop){ //f:a2
			console.log('start',_msg,_name,_stop); //f:a3
			if(_stop){ //f:b1
				if(_name==name){ //f:b2
					clearTimeout(timer); //f:b3
					show(id,msg);
					msg=name=null; //f:b4
					if(bindTitle)bindTitle(null);
				}
				return; //f:b5
			}
			clearTimeout(timer); //f:a4
			if(_msg){
				msg0=msg=_msg;
				if(msg.length>5)msg=msg.substring(0,5)+'...';
			}
			if(_name)name=_name;
			if(_name)bindName(_name);
			timer=setTimeout(function(){ //f:a5
				if((time=(time%2)+1)==1)show(id,"【新訊息】"+msg);else show(id,"【　　　】"+msg);
				var title=(time==1)?"【新訊息】"+msg:"【　　　】"+msg;
				if(bindTitle)bindTitle(title);
				start(); //f:a6
			},500);
		}
		this.start=start;
		this.inSleep=function(){return name==null;};
		function bindName(name){
			$(id).unbind('click').bind('click',function(){
				if(msg0!=$(this).html() && msg0.indexOf($(this).html().replace('...',''))!=-1)$(this).html(msg0);else
				if($(this).html()!='&nbsp;')$(this).html('&nbsp;');else $(this).html(msg0);
				if(bindBtn)bindBtn(name);
				// $(this).unbind('click');
			});
		}
	}
	var bindBtn,bindTitle;
	this.setBindBtn=function(_bindBtn){bindBtn=_bindBtn;};
	this.setBindTitle=function(_bindTitle){bindTitle=_bindTitle;};
}
function MsgPopup(id){
//@3:132
/*
 * 132=member+sendRedirect.getRequestURI.getResponseHeader+bindCancel.redirect
	$("a:jqmData(role='button')",id).unbind('click').bind('click',function(){
		setTimeout(function(){document.URL=location.href='page?chatroom';},1000);		
	});
*/
	this.popup=function(msg1,msg2,msg3,url){
		$(":jqmData(role='header') h1",id).empty().html(msg1);
		$(":jqmData(role='content') .ui-title",id).empty().html(msg2);
		$(":jqmData(role='content') p",id).empty().html(msg3);
		$(id).popup({
			afteropen:function(event,ui){bindCancel(url);},
			afterclose:function(event,ui){redirect(url);}
		}).popup('open');
	}
	function bindCancel(url){
		console.log(arguments.callee.name,'url='+url);
		$("a:jqmData(role='button')",id).unbind('click').bind('click',function(){
			if(url) $.mobile.loading("show",{text:url,textVisible:true,theme:"c",html:""});
		});
	}
	function redirect(url){
		console.log(arguments.callee.name,'url='+url);
		if(url) setTimeout(function(){document.URL=location.href=url;},1500);
	}
}