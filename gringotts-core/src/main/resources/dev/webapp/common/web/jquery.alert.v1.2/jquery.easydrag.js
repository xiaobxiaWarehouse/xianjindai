(function($) {
	var isMouseDown = false;
	var currentElement = null;
	var dropCallbacks = {};
	var dragCallbacks = {};
	var bubblings = {};
	var lastMouseX;
	var lastMouseY;
	var lastElemTop;
	var lastElemLeft;
	var dragStatus = {};
	var holdingHandler = false;
	$.getMousePosition = function(e) {
		var posx = 0;
		var posy = 0;
		if (!e)
			var e = window.event;
		if (e.pageX || e.pageY) {
			posx = e.pageX;
			posy = e.pageY;
		} else if (e.clientX || e.clientY) {
			posx = e.clientX + document.body.scrollLeft
					+ document.documentElement.scrollLeft;
			posy = e.clientY + document.body.scrollTop
					+ document.documentElement.scrollTop;
		}
		return {
			'x' : posx,
			'y' : posy
		};
	};
	$.updatePosition = function(e) {
		var pos = $.getMousePosition(e);
		var spanX = (pos.x - lastMouseX);
		var spanY = (pos.y - lastMouseY);
		$(currentElement).css("top", (lastElemTop + spanY));
		$(currentElement).css("left", (lastElemLeft + spanX));
	};
	$(document).mousemove(function(e) {
		if (isMouseDown && dragStatus[currentElement.id] != 'false') {
			$.updatePosition(e);
			if (dragCallbacks[currentElement.id] != undefined) {
				dragCallbacks[currentElement.id](e, currentElement);
			}
			return false;
		}
	});
	$(document).mouseup(function(e) {
		if (isMouseDown && dragStatus[currentElement.id] != 'false') {
			isMouseDown = false;
			if (dropCallbacks[currentElement.id] != undefined) {
				dropCallbacks[currentElement.id](e, currentElement);
			}
			return false;
		}
	});
	$.fn.ondrag = function(callback) {
		return this.each(function() {
			dragCallbacks[this.id] = callback;
		});
	};
	$.fn.ondrop = function(callback) {
		return this.each(function() {
			dropCallbacks[this.id] = callback;
		});
	};
	$.fn.dragOff = function() {
		return this.each(function() {
			dragStatus[this.id] = 'off';
		});
	};
	$.fn.dragOn = function() {
		return this.each(function() {
			dragStatus[this.id] = 'on';
		});
	};
	$.fn.setHandler = function(handlerId) {
		return this.each(function() {
			var draggable = this;
			bubblings[this.id] = true;
			$(draggable).css("cursor", "");
			dragStatus[draggable.id] = "handler";
			$("#" + handlerId).css("cursor", "move");
			$("#" + handlerId).mousedown(function(e) {
				holdingHandler = true;
				$(draggable).trigger('mousedown', e);
			});
			$("#" + handlerId).mouseup(function(e) {
				holdingHandler = false;
			});
		});
	}
	$.fn.easydrag = function(allowBubbling) {
		return this
				.each(function() {
					if (undefined == this.id || !this.id.length)
						this.id = "easydrag" + (new Date().getTime());
					bubblings[this.id] = allowBubbling ? true : false;
					dragStatus[this.id] = "on";
					$(this).css("cursor", "move");
					$(this)
							.mousedown(
									function(e) {
										if ((dragStatus[this.id] == "off")
												|| (dragStatus[this.id] == "handler" && !holdingHandler))
											return bubblings[this.id];
										$(this).css("position", "absolute");
										$(this)
												.css(
														"z-index",
														parseInt(new Date()
																.getTime() / 1000));
										isMouseDown = true;
										currentElement = this;
										var pos = $.getMousePosition(e);
										lastMouseX = pos.x;
										lastMouseY = pos.y;
										lastElemTop = this.offsetTop;
										lastElemLeft = this.offsetLeft;
										$.updatePosition(e);
										return bubblings[this.id];
									});
				});
	};
})(jQuery);