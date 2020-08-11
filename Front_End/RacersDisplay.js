
var data_url = "http://localhost:7000/";

$(document).ready(function () {

  speed = 5000;
  var scrollHandler = '';
  var previousresult = 'null';
  var previousCount = 0;

  $('body').append("</p>");

 
  function newprocessData(result){
    var onetotenracers=$("#onetotenracers > div").size();
    var numlastChildren=$("#eleventonracers > div").size() + onetotenracers ;   

    // document.getElementById("flagcolorid").style.background = result.current_flag ;
    var flag = document.getElementById("flagcolorid");
    flag.classList.remove();
    flag.classList.remove('flag-green', 'flag-red', 'flag-blue', 'flag-yellow', 'flag-finish', 'flag-checkered', 'flag-warmup');
    flag.classList.add('flag-' + result.current_flag);
    
    for (var ctr = 1 ; ctr <= onetotenracers ; ctr ++){
		$('#racerrecordid' + ctr).find('#position' + ctr ).html(ctr); // result.racers[ctr - 1 ].position_in_run
		$('#racerrecordid' + ctr).find('#name' + ctr ).html('#' + result.racers[ctr -1].racer_number + ' ' + result.racers[ctr -1].racer_name.substring(0, 28) + ' [' + result.racers[ctr -1].racer_class_name.substring(0, 2) + ']');
		$('#racerrecordid' + ctr).find('#laps'     + ctr).html(result.racers[ctr -1].current_lap); 
		$('#racerrecordid' + ctr).find('#last_lap_time' + ctr).html(trimLapTime(result.racers[ctr -1].last_lap_time)); 
		$('#racerrecordid' + ctr).find('#best_lap'  + ctr).html(result.racers[ctr -1].best_lap_number); 
		$('#racerrecordid' + ctr).find('#best_lap_time' + ctr).html(trimLapTime(result.racers[ctr -1].best_lap_time)); 
      
		if( (previousresult != 'null') 
		    && (result.racers[ctr -1].current_lap > 0)
		    &&( (result.racers[ctr -1].racer_name != previousresult.racers[ctr -1].racer_name)
		    || (result.racers[ctr -1].current_lap != previousresult.racers[ctr -1].current_lap) )
		  ) 
		{
		    $('#racerrecordid' + ctr).effect( "highlight" , {color: '#0000FF'} , 2000);

            if (result.racers[ctr -1].best_lap_number == result.racers[ctr -1].current_lap)
            	$('#racerrecordid' + ctr).find('.headers').addClass('best');
            else
            	$('#racerrecordid' + ctr).find('.headers').removeClass('best');

		}
    }

    for (var ctr = onetotenracers + 1  ; ctr <= result.racers.length  ; ctr ++){
        if(ctr > numlastChildren)
        {
          $("#racerrecordid2" ).clone().attr({ id: 'racerrecordid'+ctr}).appendTo( "#eleventonracers" );
          $('#racerrecordid' + ctr).find('#headers2').prop({ id: 'headers' + ctr }).css( "margin-top" , "1px").css ("font-size" ,"10px");
          $('#racerrecordid' + ctr).find('#name2').prop({ id: 'name' + ctr }).css ("font-size" ,"15px").css("margin-top","7px").css("color","white").css( "display" , "inline-block"); 
          $('#racerrecordid' +  ctr).find('#position2').prop({ id: 'position' + ctr }).css( "width" , "7%" ).css("display" , "inline-block").css("font-size" , "15px").css("color","white");
          $('#racerrecordid' + ctr).find('#laps2').prop({ id: 'laps' + ctr}).css( "width" , "11%" ).css("display" , "inline-block").css("font-size" , "15px").css("color","white");
          $('#racerrecordid' + ctr).find('#last_lap_time2').prop({ id: 'last_lap_time' + ctr }).css( "width" , "29%" ).css("display" , "inline-block").css("font-size" , "15px").css("color","white");
          $('#racerrecordid' + ctr).find('#best_lap2').prop({ id: 'best_lap' + ctr }).css( "width" , "20%" ).css("display" , "inline-block").css("font-size" , "15px").css("color","white");
          $('#racerrecordid' + ctr).find('#best_lap_time2').prop({ id: 'best_lap_time' + ctr }).css( "width" , "29%" ).css("display" , "inline-block").css("font-size" , "15px").css("color","white");
        }

        $('#name' + ctr).html('#' + result.racers[ctr -1].racer_number + ' ' + result.racers[ctr -1].racer_name.substring(0, 28) + ' [' + result.racers[ctr -1].racer_class_name.substring(0, 2) + ']');
        $('#position' +  ctr).html(ctr); // result.racers[ctr - 1].position_in_run        
        $('#laps' + ctr).html(result.racers[ctr - 1].current_lap); 
        $('#last_lap_time' + ctr).html(trimLapTime(result.racers[ctr -1].last_lap_time)); 
        $('#best_lap' + ctr).html(result.racers[ctr -1].best_lap_number); 
        $('#best_lap_time' + ctr).html(trimLapTime(result.racers[ctr -1].best_lap_time)); 
        $("#name11").css("margin-top","0px");
        $("#name10").css("margin-bottom","0px");
        
        
        if( (previousresult != 'null') 
	        &&  (  ctr <= previousresult.racers.length )
	        &&  (result.racers[ctr -1].current_lap > 0)
	        &&( (result.racers[ctr -1].racer_name != previousresult.racers[ctr -1].racer_name)
	        || (result.racers[ctr -1].current_lap != previousresult.racers[ctr -1].current_lap) )
	       )
        {
        	$('#racerrecordid' + ctr).effect( "highlight" , {color: '#0000FF'} , 2000);

            if (result.racers[ctr -1].best_lap_number == result.racers[ctr -1].current_lap)
            	$('#racerrecordid' + ctr).find('.headers').addClass('best');
            else
            	$('#racerrecordid' + ctr).find('.headers').removeClass('best');
        }
    }

    // determine speed based on # of racers
    speed = getScrollTime(result.racers.length);
    
    // clear the scroll interval and adjust if the racer count has changed
    if (result.racers.length != previousCount)
    	setupScrolling();

    previousresult = result;
    previousCount = result.racers.length;
      
    for (ctr = result.racers.length +1 ; ctr <= numlastChildren ;ctr++  ){
      $('#racerrecordid'+ctr).remove();
    }
    
    
  }

  function trimLapTime(time) {
	  if (typeof time == 'undefined' || time === null)
		  return "";
	  
	  var time = time.trim();

	  if (time.substring(0, 5) == "00:00")
		  return time.substring(5, time.length);
	  else if (time.substring(0, 4) == "00:0")
		  return time.substring(4, time.length);
	  else if (time.substring(0, 3) == "00:")
		  return time.substring(3, time.length);
	  else if (time.substring(0, 1) == "0")
		  return time.substring(1, time.length);
	  else
		  return time;
  }
  
  function getScrollTime(numberOfRacers){
	var delay = 1000;
    return (numberOfRacers > 20) ? (numberOfRacers - 20) * delay : 0;
  }

  function scroll(speed) {
	  console.log("starting scroll(" + speed + ") for distance of " + ($('#eleventonracers').prop('scrollHeight') - $('#eleventonracers').height()));
      $('#eleventonracers').animate({
      	duration: speed,
          scrollTop: $('#eleventonracers').prop('scrollHeight') - $('#eleventonracers').height(),
          easing: 'linear'
      }, {
          duration: speed,
          easing: 'linear',
          complete: function () {
        	  console.log("starting complete(" + speed + ")");
              $(this).animate({
                  scrollTop: 0
              }, {
                  duration: speed,
                  easing: 'linear',
                  complete: speed
              });
          }
      });
  }

  
  function setupScrolling() {
	  if (scrollHandler) {
		  console.log("Clearing scrollHandler");
		  clearInterval(scrollHandler);
	  }
	  
	  console.log("calling function scroll(" + speed + ")");
	  scroll(speed)
	  scrollHandler = setInterval(function () {
		  console.log("calling function scroll(" + speed + ") @ 2 x speed");
	      scroll(speed)
	  }, speed * 2);
  }

  
  function fetchData() { 
	  $.ajax({
		  type: "GET",    
		  url: data_url, 
		  beforeSend: function(xhr){
		        if (xhr.overrideMimeType)
		        {
		          xhr.overrideMimeType("application/json");
		        }
		      },
		   dataType: 'json',
		   success: function(result)
		   {
		       newprocessData(result);
		   },
		   error: function (data, status, error) {
		       console.log('error is = ', error);
		   }
		});
  }
  
  
  // initial load
  fetchData();
  var ajaxHandler = setInterval(fetchData, 10 * 1000);
  
});
