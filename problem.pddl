(define (problem smashpb)

	(:domain smashdm)


	(:objects
		true
		_q5q__q6q_
		_q1q_maxphone_q1q_
		_q1q_max_q1q_
		_q1q_livingroom_q1q_
		_q1q_standby_q1q_
		_q1q_standard_q1q_
		_q1q_none_q1q_
		_q1q_maxsofa_q1q_
		_q1q_maxtv_q1q_
		_q1q_off_q1q_
		_q1q_sarah_q1q_
		_q1q_offduty_q1q_
		_q1q_mute_q1q_
		_q1q_unmute_q1q_
		_q1q_playing_q1q_
		_q1q_hangingout_q1q_
		_q1q_tv_q1q_
		_q1q_furniture_q1q_
		_q1q_phone_q1q_
		vbc
		_q1q_benevolencecaring_q1q_
		vbd
		_q1q_benevolencedependability_q1q_
		vuc
		_q1q_universalismconcern_q1q_
		vun
		_q1q_universalismnature_q1q_
		vut
		_q1q_universalismtolerance_q1q_
		vst
		_q1q_selfdirectionthought_q1q_
		vsa
		_q1q_selfdirectionaction_q1q_
		vsi
		_q1q_stimulation_q1q_
		vhe
		_q1q_hedonism_q1q_
		vac
		_q1q_achievement_q1q_
		vpd
		_q1q_powerdominance_q1q_
		vpr
		_q1q_powerresources_q1q_
		vfa
		_q1q_face_q1q_
		vsp
		_q1q_securitypersonal_q1q_
		vss
		_q1q_securitysocietal_q1q_
		vtr
		_q1q_tradition_q1q_
		vcr
		_q1q_conformityrules_q1q_
		vci
		_q1q_conformityinterpersonal_q1q_
		vhu
		_q1q_humility_q1q_
		cobj_2
		home
		_q1q__q2q_main_q2q_home_q1q_
		cobj_0
		main
		_q1q__q2q_main_q1q_
		_q1q_meeting_q1q_
		_q1q_tomorrow_q1q_
		_q1q_busy_q1q_
		_q1q_voicemail_q1q_
		_q1q_ringing_q1q_
		_q1q_receivingcall_q1q_
		cobj_7
		maxtv
		_q1q_devices_q7q_arttv_q1q_
		cobj_6
		maxsofa
		_q1q_devices_q7q_artsofa_q1q_
		cobj_5
		maxphone
		_q1q_devices_q7q_artphone_q1q_
		cobj_3
		mycontext
		_q1q_context_q7q_context_q1q_
		vbegin
		vend
		valuemostimportant
		_
		valuemoreimportantthan
		valuelessimportant
		false
		active
		user
		self
		dropped
		turnon_q3q__q1q_maxtv_q1q__q4q_
		watch_q3q__q1q_max_q1q__q8q__q1q_canal+_q1q__q4q_
		negative
		settvstatus
		setphonestatus
	)


	(:init
		(addphone _q1q_maxphone_q1q_ _q1q_max_q1q_ _q1q_livingroom_q1q_ _q1q_standby_q1q_ _q1q_standard_q1q_)
		(standingup _q1q_none_q1q_ _q1q_maxsofa_q1q_)
		(addtv _q1q_maxtv_q1q_ _q1q_max_q1q_ _q1q_livingroom_q1q_ _q1q_off_q1q_)
		(sittingon _q1q_none_q1q_ _q1q_maxsofa_q1q_)
		(sittingon _q1q_none_q1q_ _q1q_maxsofa_q1q_)
		(workingstatus _q1q_sarah_q1q_ _q1q_offduty_q1q_)
		(workingstatus _q1q_max_q1q_ _q1q_offduty_q1q_)
		(person _q1q_sarah_q1q_)
		(person _q1q_max_q1q_)
		(callertype _q1q_none_q1q_ _q1q_none_q1q_)
		(addfurniture _q1q_maxsofa_q1q_ _q1q_livingroom_q1q_)
		(possibletvstatus _q1q_off_q1q_ _q1q_standby_q1q_ _q1q_mute_q1q_ _q1q_unmute_q1q_ _q1q_playing_q1q_)
		(userstatus _q1q_sarah_q1q_ _q1q_hangingout_q1q_)
		(userstatus _q1q_max_q1q_ _q1q_hangingout_q1q_)
		(sensing _q1q_maxsofa_q1q_ _q1q_none_q1q_)
		(incomingcall _q1q_maxphone_q1q_ _q1q_none_q1q_)
		(addperson _q1q_sarah_q1q_ _q1q_hangingout_q1q_ _q1q_offduty_q1q_)
		(addperson _q1q_max_q1q_ _q1q_hangingout_q1q_ _q1q_offduty_q1q_)
		(type _q1q_maxtv_q1q_ _q1q_tv_q1q_)
		(type _q1q_maxsofa_q1q_ _q1q_furniture_q1q_)
		(type _q1q_maxphone_q1q_ _q1q_phone_q1q_)
		(devicemode _q1q_maxphone_q1q_ _q1q_standard_q1q_)
		(deviceowner _q1q_maxtv_q1q_ _q1q_max_q1q_)
		(deviceowner _q1q_maxphone_q1q_ _q1q_max_q1q_)
		(hasvaluename vbc _q1q_benevolencecaring_q1q_)
		(hasvaluename vbd _q1q_benevolencedependability_q1q_)
		(hasvaluename vuc _q1q_universalismconcern_q1q_)
		(hasvaluename vun _q1q_universalismnature_q1q_)
		(hasvaluename vut _q1q_universalismtolerance_q1q_)
		(hasvaluename vst _q1q_selfdirectionthought_q1q_)
		(hasvaluename vsa _q1q_selfdirectionaction_q1q_)
		(hasvaluename vsi _q1q_stimulation_q1q_)
		(hasvaluename vhe _q1q_hedonism_q1q_)
		(hasvaluename vac _q1q_achievement_q1q_)
		(hasvaluename vpd _q1q_powerdominance_q1q_)
		(hasvaluename vpr _q1q_powerresources_q1q_)
		(hasvaluename vfa _q1q_face_q1q_)
		(hasvaluename vsp _q1q_securitypersonal_q1q_)
		(hasvaluename vss _q1q_securitysocietal_q1q_)
		(hasvaluename vtr _q1q_tradition_q1q_)
		(hasvaluename vcr _q1q_conformityrules_q1q_)
		(hasvaluename vci _q1q_conformityinterpersonal_q1q_)
		(hasvaluename vhu _q1q_humility_q1q_)
		(devicelocation _q1q_maxtv_q1q_ _q1q_livingroom_q1q_)
		(devicelocation _q1q_maxsofa_q1q_ _q1q_livingroom_q1q_)
		(devicelocation _q1q_maxphone_q1q_ _q1q_livingroom_q1q_)
		(joinedwsp cobj_2 home _q1q__q2q_main_q2q_home_q1q_)
		(joinedwsp cobj_0 main _q1q__q2q_main_q1q_)
		(addevent _q1q_max_q1q_ _q1q_meeting_q1q_ _q1q_tomorrow_q1q_)
		(device _q1q_maxtv_q1q_)
		(device _q1q_maxsofa_q1q_)
		(device _q1q_maxphone_q1q_)
		(possiblephonestatus _q1q_standby_q1q_ _q1q_off_q1q_ _q1q_busy_q1q_ _q1q_voicemail_q1q_ _q1q_mute_q1q_ _q1q_unmute_q1q_ _q1q_ringing_q1q_ _q1q_receivingcall_q1q_)
		(deviceneighbor _q1q_maxtv_q1q_ _q1q_maxtv_q1q_)
		(deviceneighbor _q1q_maxtv_q1q_ _q1q_maxsofa_q1q_)
		(deviceneighbor _q1q_maxtv_q1q_ _q1q_maxphone_q1q_)
		(deviceneighbor _q1q_maxsofa_q1q_ _q1q_maxtv_q1q_)
		(deviceneighbor _q1q_maxsofa_q1q_ _q1q_maxsofa_q1q_)
		(deviceneighbor _q1q_maxsofa_q1q_ _q1q_maxphone_q1q_)
		(deviceneighbor _q1q_maxphone_q1q_ _q1q_maxtv_q1q_)
		(deviceneighbor _q1q_maxphone_q1q_ _q1q_maxsofa_q1q_)
		(deviceneighbor _q1q_maxphone_q1q_ _q1q_maxphone_q1q_)
		(hasworkingevent _q1q_max_q1q_ _q1q_tomorrow_q1q_)
		(focusing cobj_7 maxtv _q1q_devices_q7q_arttv_q1q_ cobj_2 home _q1q__q2q_main_q2q_home_q1q_)
		(focusing cobj_6 maxsofa _q1q_devices_q7q_artsofa_q1q_ cobj_2 home _q1q__q2q_main_q2q_home_q1q_)
		(focusing cobj_5 maxphone _q1q_devices_q7q_artphone_q1q_ cobj_2 home _q1q__q2q_main_q2q_home_q1q_)
		(focusing cobj_3 mycontext _q1q_context_q7q_context_q1q_ cobj_2 home _q1q__q2q_main_q2q_home_q1q_)
		(outcomingcall _q1q_none_q1q_ _q1q_maxphone_q1q_)
		(devicestatus _q1q_maxtv_q1q_ _q1q_off_q1q_)
		(devicestatus _q1q_maxphone_q1q_ _q1q_standby_q1q_)
		(gl--debug true)
		(gl--state turnon_q3q__q1q_maxtv_q1q__q4q_ active self)
		(gl--state watch_q3q__q1q_max_q1q__q8q__q1q_canal+_q1q__q4q_ active user)
		(demo--demo false)
	)


	(:goal
		(devicestatus _q1q_maxtv_q1q_ _q1q_standby_q1q_)
	)

)