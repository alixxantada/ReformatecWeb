/******************************

[Table of Contents]

1. Vars and Inits
2. Set Header
3. Init Home Slider
4. Init Menu
5. Init Isotope Filtering
6. Init Search
7. Init Search buscador avanzado
8. Init Listing Slider
9. Init Lightbox
10. Init CTA Slider
11. Init Testimonials Slider
12. Init Search Form
13. Init More Options

******************************/

$(document).ready(function()
{
	"use strict";

	/* 

	1. Vars and Inits

	*/

	var menu = $('.menu');
	var menuActive = false;
	var header = $('.header');
	var searchActive = false;

	setHeader();

	$(window).on('resize', function()
	{
		setHeader();
	});

	$(document).on('scroll', function()
	{
		setHeader();
	});

	initHomeSlider();
	initMenu();
	initIsotopeFiltering();
	initSearch();
	initSearch2();
	initListingSlider();
	initLightbox();
	initCtaSlider();
	initTestSlider();
	initSearchForm();	
	initMoreOptions();	

	/* 

	2. Set Header

	*/

	function setHeader()
	{
		if(window.innerWidth < 992)
		{
			if($(window).scrollTop() > 100)
			{
				header.addClass('scrolled');
			}
			else
			{
				header.removeClass('scrolled');
			}
		}
		else
		{
			if($(window).scrollTop() > 100)
			{
				header.addClass('scrolled');
			}
			else
			{
				header.removeClass('scrolled');
			}
		}
		if(window.innerWidth > 991 && menuActive)
		{
			closeMenu();
		}
	}






	/* 

	3. Init Home Slider

	*/

	function initHomeSlider()
	{
		if($('.home_slider').length)
		{
			var homeSlider = $('.home_slider');

			homeSlider.owlCarousel(
			{
				items:1,
				loop:true,
				autoplay:false,
				smartSpeed:1200,
				dotsContainer:'main_slider_custom_dots'
			});

			/* Custom nav events */
			if($('.home_slider_prev').length)
			{
				var prev = $('.home_slider_prev');

				prev.on('click', function()
				{
					homeSlider.trigger('prev.owl.carousel');
				});
			}

			if($('.home_slider_next').length)
			{
				var next = $('.home_slider_next');

				next.on('click', function()
				{
					homeSlider.trigger('next.owl.carousel');
				});
			}

			/* Custom dots events */
			if($('.home_slider_custom_dot').length)
			{
				$('.home_slider_custom_dot').on('click', function()
				{
					$('.home_slider_custom_dot').removeClass('active');
					$(this).addClass('active');
					homeSlider.trigger('to.owl.carousel', [$(this).index(), 300]);
				});
			}

			/* Change active class for dots when slide changes by nav or touch */
			homeSlider.on('changed.owl.carousel', function(event)
			{
				$('.home_slider_custom_dot').removeClass('active');
				$('.home_slider_custom_dots li').eq(event.page.index).addClass('active');
			});	

			// add animate.css class(es) to the elements to be animated
			function setAnimation ( _elem, _InOut )
			{
				// Store all animationend event name in a string.
				// cf animate.css documentation
				var animationEndEvent = 'webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend';

				_elem.each ( function ()
				{
					var $elem = $(this);
					var $animationType = 'animated ' + $elem.data( 'animation-' + _InOut );

					$elem.addClass($animationType).one(animationEndEvent, function ()
					{
						$elem.removeClass($animationType); // remove animate.css Class at the end of the animations
					});
				});
			}

			// Fired before current slide change
			homeSlider.on('change.owl.carousel', function(event)
			{
				var $currentItem = $('.home_slider_item', homeSlider).eq(event.item.index);
				var $elemsToanim = $currentItem.find("[data-animation-out]");
				setAnimation ($elemsToanim, 'out');
			});

			// Fired after current slide has been changed
			homeSlider.on('changed.owl.carousel', function(event)
			{
				var $currentItem = $('.home_slider_item', homeSlider).eq(event.item.index);
				var $elemsToanim = $currentItem.find("[data-animation-in]");
				setAnimation ($elemsToanim, 'in');
			})
		}
	}





	/* 

	4. Init Menu

	*/

	function initMenu()
	{
		if($('.hamburger').length && $('.menu').length)
		{
			var hamb = $('.hamburger');
			var close = $('.menu_close_container');

			hamb.on('click', function()
			{
				if(!menuActive)
				{
					openMenu();
				}
				else
				{
					closeMenu();
				}
			});

			close.on('click', function()
			{
				if(!menuActive)
				{
					openMenu();
				}
				else
				{
					closeMenu();
				}
			});

	
		}
	}

	function openMenu()
	{
		menu.addClass('active');
		menuActive = true;
	}

	function closeMenu()
	{
		menu.removeClass('active');
		menuActive = false;
	}






	/* 

	5 Init Isotope Filtering

	*/

    function initIsotopeFiltering()
    {
    	var sortBtn = $('.sort_btn');
    	var filterBtn = $('.filter_btn');

    	if($('.offers_grid').length)
    	{
    		var grid = $('.offers_grid').isotope({
    			itemSelector: '.offers_item',
	            getSortData: {
	            	price: function(itemElement)
	            	{
	            		var priceEle = $(itemElement).find('.offers_price').text().replace( '$', '' );
	            		return parseFloat(priceEle);
	            	},
	            	name: '.offer_name',
	            	stars: function(itemElement)
	            	{
	            		var starsEle = $(itemElement).find('.offers_rating');
	            		var stars = starsEle.attr("data-rating");
	            		return stars;
	            	}
	            },
	            animationOptions: {
	                duration: 750,
	                easing: 'linear',
	                queue: false
	            }
	        });

    		// Sorting
	        sortBtn.each(function()
	        {
	        	$(this).on('click', function()
	        	{
	        		var parent = $(this).parent().parent().find('.sorting_text');
	        		parent.text($(this).text());
	        		var option = $(this).attr('data-isotope-option');
	        		option = JSON.parse( option );
    				grid.isotope( option );
	        	});
	        });

	        // Filtering
	        $('.filter_btn').on('click', function()
	        {
	        	var parent = $(this).parent().parent().find('.sorting_text');
	        	parent.text($(this).text());
		        var filterValue = $(this).attr('data-filter');
  				grid.isotope({ filter: filterValue });
	        });
    	}
    }




	/* 

	6. Init Search

	*/

	function initSearch()
	{
		if($('.search_tab').length)
		{
			$('.search_tab').on('click', function()
			{
				$('.search_tab').removeClass('active');
				$(this).addClass('active');
				var clickedIndex = $('.search_tab').index(this);

				var panels = $('.search_panel');
				panels.removeClass('active');
				$(panels[clickedIndex]).addClass('active');
			});
		}
	}






	/* 

	7. Init Search buscador avanzado

	*/

	function initSearch2()
	{
		if($('.search2_tab').length)
		{
			$('.search2_tab').on('click', function()
			{
				$('.search2_tab').removeClass('active');
				$(this).addClass('active');
				var clickedIndex = $('.search2_tab').index(this);

				var panels = $('.search2_panel');
				panels.removeClass('active');
				$(panels[clickedIndex]).addClass('active');
			});
		}
	}




	
	/* 

	8. Init Listing Slider

	*/

	function initListingSlider()
	{
		if($('.hotel_slider').length)
		{
			var hotelSlider = $('.hotel_slider');

			hotelSlider.owlCarousel(
			{
				loop:true,
				nav:false,
				dots:false,
				margin:16,
				responsive:
				{
					0:{items:2},
					320:{items:3},
					480:{items:4},
					575:{items:5},
					768:{items:7},
					992:{items:8},
					1199:{items:9}
				}
			});

			/* Custom nav events */
			if($('.hotel_slider_prev').length)
			{
				var prev = $('.hotel_slider_prev');

				prev.on('click', function()
				{
					hotelSlider.trigger('prev.owl.carousel');
				});
			}

			if($('.hotel_slider_next').length)
			{
				var next = $('.hotel_slider_next');

				next.on('click', function()
				{
					hotelSlider.trigger('next.owl.carousel');
				});
			}
		}
	}






	/*

	9. Init Lightbox

	*/

	function initLightbox()
	{
		if($('.cboxElement').length)
		{
			$('.colorbox').colorbox(
			{
				rel:'colorbox'
			});
		}
	}
	

	

	/*
	
	10. Init CTA Slider

	*/

	function initCtaSlider()
	{
		if($('.cta_slider').length)
		{
			var ctaSlider = $('.cta_slider');

			ctaSlider.owlCarousel(
			{
				items:1,
				loop:true,
				autoplay:false,
				nav:false,
				dots:false,
				smartSpeed:1200
			});

			/* Custom nav events */
			if($('.cta_slider_prev').length)
			{
				var prev = $('.cta_slider_prev');

				prev.on('click', function()
				{
					ctaSlider.trigger('prev.owl.carousel');
				});
			}

			if($('.cta_slider_next').length)
			{
				var next = $('.cta_slider_next');

				next.on('click', function()
				{
					ctaSlider.trigger('next.owl.carousel');
				});
			}
		}
	}






	/* 

	11. Init Testimonials Slider

	*/

	function initTestSlider()
	{
		if($('.test_slider').length)
		{
			var testSlider = $('.test_slider');

			testSlider.owlCarousel(
			{
				loop:true,
				nav:false,
				dots:false,
				smartSpeed:1200,
				margin:30,
				responsive:
				{
					0:{items:1},
					480:{items:1},
					768:{items:2},
					992:{items:3}
				}
			});

			/* Custom nav events */
			if($('.test_slider_prev').length)
			{
				var prev = $('.test_slider_prev');

				prev.on('click', function()
				{
					testSlider.trigger('prev.owl.carousel');
				});
			}

			if($('.test_slider_next').length)
			{
				var next = $('.test_slider_next');

				next.on('click', function()
				{
					testSlider.trigger('next.owl.carousel');
				});
			}
		}
	}






	/* 

	12. Init Search Form

	*/

	function initSearchForm()
	{
		if($('.search_form').length)
		{
			var searchForm = $('.search_form');
			var searchInput = $('.search_content_input');
			var searchButton = $('.content_search');

			searchButton.on('click', function(event)
			{
				event.stopPropagation();

				if(!searchActive)
				{
					searchForm.addClass('active');
					searchActive = true;

					$(document).one('click', function closeForm(e)
					{
						if($(e.target).hasClass('search_content_input'))
						{
							$(document).one('click', closeForm);
						}
						else
						{
							searchForm.removeClass('active');
							searchActive = false;
						}
					});
				}
				else
				{
					searchForm.removeClass('active');
					searchActive = false;
				}
			});	
		}
	}






	/* 

	13. Init More Options

	*/

	function initMoreOptions()
	{
		if($('.more_options').length)
		{
			var triggerEle = $('.more_options_trigger');
			var ele = $('.more_options_list');

			triggerEle.on('click', function(e)
			{
				e.preventDefault();
				triggerEle.toggleClass('active');
				ele.toggleClass('active');

				var panel = ele;
				var panelH = ele.prop('scrollHeight') + "px";
				
				if(panel.css('max-height') == "0px")
				{
					panel.css('max-height', panel.prop('scrollHeight') + "px");
				}
				else
				{
					panel.css('max-height', "0px");
				} 
			});
		}
	}		
});