/**
 * @作者 田应平
 * @创建时间 2018-04-23 11:05
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
;(function(){

    window.winFn = {
        /**area -->[width,height]*/
        addOrEdit : function(title,domDivId,area,closedCall,btn3,btn3Call){
            var btns = (btn3 == null || btn3 == '') ? ['提交','取消'] : ['提交','取消',btn3];
            return layer.open({
                type : 1,
                title : title,
                content : $(domDivId),//最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                area : area,
                resize : false,
                btn : btns,
                yes : function(index,layero){
                    if (closedCall != null && closedCall != ''){
                        closedCall(index,layero);
                    }
                },
                btn3 : function(index,layero){
                    if (btn3Call != null && btn3Call != '' && btn3 != null && btn3 != ''){
                        btn3Call(index,layero);
                    }
                    return false;
                }
            });
        },
        login : function(title,domDivId,area,closedCall){
            return layer.open({
                type : 1,
                title : title,
                content : $(domDivId),
                area : area,
                resize : false,
                closeBtn: 0,
                btn : '提交',
                yes : function(index,layero){
                    if (closedCall != null && closedCall != ''){
                        closedCall(index,layero);
                    }
                },
            });
        },
        ajaxPost : function(url,params,succeed,failure){
            $.ajax({
                type : "POST",
                url : url,
                dataType : "json",
                data : params,
                success : function(result){
                    succeed(result);
                },
                error : function(response,err){
                    if (failure != null && failure != ''){
                        failure(err);
                    }
                }
            });
        },
        randomWord : function(){
            var chars = ['1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','P','Q','R','S','T','U','V','W','X','Y','Z'];
            var res = "";
            for(var i = 0; i < 4 ; i ++){
                var id = Math.ceil(Math.random() * 31);
                res += chars[id];
            }
            return res;
        },
        inputRequired : function(dom){
            var value = winFn.getDomValue(dom);
            if (winFn.isEmpty(value)){
                return true;
            } else {
                return false;
            }
        },
        getDomValue : function(dom){
            if($(dom).val() == '_')return null;
            return $(dom).val();
        },
        isEmpty : function(object){
            if(typeof object == 'function'){
                if(object == null || object == '' || object == undefined)return true;
            }
            if (object != null && object != ''){
                if(object == 'undefined' || object == 'UNDEFINED')return true;
            }
            if(object == null || object == undefined || object == '')return true;
            if($.trim(object).length > 0 && $.trim(object) == 'null')return true;
            if($.trim(object).length > 0 && $.trim(object) == 'NULL')return true;
            if(object.length == 0)return true;
            if($.trim(object).length <= 0)return true;
            if($.trim(object) == '')return true;
            return false;
        },
        loadAjax : function(msg){
            msg = (msg == null || msg == '' || msg == undefined)?'操作中,请稍候……':msg;
            return top.layer.msg(msg,{icon:16,time:-1,shade:[0.3,'#000'],area:'auto'});
        },
        msg : function (msg){
            top.layer.alert('<div>'+msg+'</div>',{
                title : '系统提示',
                area : 'auto',
                btn : ['确定'],
            });
        },
        clearFormData : function(formId){
            $(':input',''+formId).not(':button,:submit,:reset,:hidden').val('').removeAttr('selected');
            var $_obj = $(formId).find("select");
            var count = $_obj.length;
            if (count > 0){
                $($_obj).each(function(i){
                    $(this).val('');
                });
            }
        },
        tipsRb : function(msg){
            layer.open({
                title:'系统提示',
                content: msg,
                offset: 'rb',
                shade: 0,
                area:'auto',
                time: 4000
            });
        }
    },
    window.alert = function(msg,callback){
        var al_t = (self==top)?parent:window;
        al_t.layer.alert(msg,{
            title : '系统提示',
            area : 'auto',
            btn : ['确定'],
            cancel : function(index,layero){
                al_t.layer.close(index);
                if(callback != null && callback != ''){
                    callback();
                }
            }
        },function (index) {
            al_t.layer.close(index);
            if(callback != null && callback != ''){
                callback();
            }
        });
    }
    /**confirm('你好,需要服务吗?',function() {alert('好的,谢谢!',function(){alert('嗯,再见!')})});*/
    window.confirm = function(msg,callback){
        var conf_m = (self==top)?parent:window;
        conf_m.layer.confirm(msg,{
                title : '系统提示',
                btn : ['确定','取消'],
                area : 'auto',
            },
            function(){
                if(typeof(callback) === "function"){
                    callback("ok");
                }
            });
    }
    window.fwtai = {};
    /**用法:fwtai.msg('什么情况?');*/
    fwtai.msg = function (msg){
        alert(msg)
    }
})(jQuery);