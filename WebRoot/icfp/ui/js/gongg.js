$(function(){
	document.onselectstart = function(){ 
		return false; 
	};//阻止选择

	(function(undef) {
		function getDragRects(selector){
			var dragRects = [];
			$(selector).each(function(index, item) {
				var jItem = $(item),
				offset = jItem.offset();
				dragRects.push(item.rect = {
					index: index,
					drag: item,
					left: offset.left,
					top: offset.top,
					right: offset.left + jItem.width(),
					bottom: offset.top + jItem.height()
				});
			});
			return dragRects;
		}
		var dragObj, rects, startX, startY, boxX, boxY, dragHelper, passDrag, sx, sy, cx, cy, downObj, dragStart;
		var dragStart = false;
		$('.tile img').live('mousedown', function(e) {
			e = e||event;
			var oDragHandle = e ? e.target : event.srcElement;
			sx = e.clientX,
		    sy = e.clientY;
			dragStart = true;
			downObj = $(this).parents('.tile')[0];
		});
		
		
		 
		$('.tile img').live('dragstart',function(e){                
			return false;            
		});
		 
		$(document).live('mousemove', function(e){
			e = e||event;
			cx = e.clientX;
			cy = e.clientY;
			if(!dragObj){
				if(!dragStart){
					return false;
				}
				if(Math.max(Math.abs(cx - sx), Math.abs(cy - sy)) < 2) {
					return false;
				}
				var self = downObj,
		        jSelf = $(self);
		        rects = getDragRects('.tile:not(.ui-state-disabled)');
		        dragObj = self;
		        startX = e.pageX;
		        startY = e.pageY;
		        boxX = jSelf.offset().left - startX;
		        boxY = jSelf.offset().top - startY;
		        if(dragHelper){
			        $(dragHelper).stop(true, true);
				}
		        dragHelper = document.createElement('div');
				dragHelper.innerHTML = dragObj.innerHTML;
				$(dragObj).css('opacity', 0);
				$(dragObj).css('visibility','hidden'); 
		        $(dragHelper).addClass('drag-helper').css({
			        width: jSelf.width() + 'px',
			        height: jSelf.height() + 'px',
			        left: jSelf.offset().left + 'px',
			        top: jSelf.offset().top + 'px'
			    });
		        document.body.appendChild(dragHelper);
			}else{
				var deltaX = e.pageX - startX;
		        var deltaY = e.pageY - startY;
		        var newPosX = startX + deltaX + boxX;
		        var newPosY = startY + deltaY + boxY;
		        $(dragHelper).css({
			        left: newPosX + 'px',
			        top: newPosY + 'px'
			    });
		        var hitDrag;
		        $(rects).each(function(index,rect){
			        if(e.pageX > rect.left && e.pageX < rect.right){
				        if(e.pageY > rect.top && e.pageY < rect.bottom){
					        if(rect.drag !== dragObj){
						        hitDrag = rect.drag;
						    }
						    return false;
						}
					}
					return true;
				});
		        if(hitDrag !== passDrag){
		        	if(passDrag){
			        	$(passDrag).removeClass('pass-drag').removeClass('pass-drag-left').removeClass('pass-drag-top').removeClass('pass-drag-right').removeClass('pass-drag-bottom');
		            }
		            passDrag = hitDrag;
		            if(!passDrag){
			            return false;
			        }
		            var dragRect = {
						left: $(dragObj).offset().left,
						top: $(dragObj).offset().top,
						right: $(dragObj).offset().left + $(dragObj).width(),
						bottom: $(dragObj).offset().top + $(dragObj).height()
	                },
	                passRect = {
						left: $(passDrag).offset().left,
						top: $(passDrag).offset().top,
						right: $(passDrag).offset().left + $(passDrag).width(),
						bottom: $(passDrag).offset().top + $(passDrag).height()
	                };
		            $(passDrag).addClass('pass-drag');
		            if(passRect.left > dragRect.left){
			            $(passDrag).addClass('pass-drag-left');
			        }else if(passRect.left < dragRect.left){
		                $(passDrag).addClass('pass-drag-right');
		            }
					if(passRect.top > dragRect.top){
					  $(passDrag).addClass('pass-drag-top');
					}else if (passRect.top < dragRect.top){
					  $(passDrag).addClass('pass-drag-bottom');
					}
		        }
			}
		});

		$(document).live('mouseup', function(e){
			e = e||event;
			dragStart = false;
			if(dragObj){
				if(Math.max(Math.abs(cx - sx), Math.abs(cy - sy)) > 2) {
					e.preventDefault();
				}
				if(passDrag){
					$(passDrag).removeClass('pass-drag').removeClass('pass-drag-left').removeClass('pass-drag-top').removeClass('pass-drag-right').removeClass('pass-drag-bottom');
					$(dragHelper).animate({
						left: passDrag.rect.left + 'px',
						top: passDrag.rect.top + 'px'
					},400,function(dragObj){
						return function(){
							if(!passDrag) {
								return false;
							};
							if(!dragHelper){
								dragHelper = $('.drag-helper');
							};
							dragObj.innerHTML = dragSwitcher.innerHTML;
							$(dragObj).css('opacity', 1);
							$(dragObj).css('visibility','visible'); 
							passDrag.innerHTML = dragHelper.innerHTML;
							$(passDrag).css('opacity', 1);
							$(passDrag).css('visibility','visible'); 
							document.body.removeChild(dragHelper);
							document.body.removeChild(dragSwitcher);
						};
					}(dragObj));
					var dragSwitcher = document.createElement('div');
					dragSwitcher.innerHTML = passDrag.innerHTML;
					$(passDrag).css('opacity', 0);
					$(passDrag).css('visibility','hidden'); 
					$(dragSwitcher).addClass('drag-switcher').css({
			            width: $(passDrag).width() + 'px',
			            height: $(passDrag).height() + 'px',
			            left: $(passDrag).offset().left + 'px',
			            top: $(passDrag).offset().top + 'px'
			        }).animate({
			            left: $(dragObj).offset().left + 'px',
			            top: $(dragObj).offset().top + 'px'
			        },400);
					document.body.appendChild(dragSwitcher);
				}else{
					$(dragHelper).animate({
						left: $(dragObj).offset().left + 'px',
						top: $(dragObj).offset().top + 'px'
					},400,function(dragObj){
						return function(){
							dragObj.innerHTML = dragHelper.innerHTML;
							$(dragObj).css('opacity', 1);
							$(dragObj).css('visibility','visible'); 
							document.body.removeChild(dragHelper);
							dragHelper = null;
						};
					}(dragObj));
				}
				dragObj = null;
			}
		});
	})();
});