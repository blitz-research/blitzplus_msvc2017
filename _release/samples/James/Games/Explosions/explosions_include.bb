

; -----------------------------------------------------------------------------
; EXPLOSION System -- placed in the public domain by james @ hi-toro.com, 2002.
; -----------------------------------------------------------------------------

; Fully commented 2D image explosion system, suitable for realtime use if either:

; * RenderExplosion () is used, or;
; * StartRealtimeExplosion () is used on small images, few in number.

; NOTE FROM EXPERIENCE! If anything odd happens with explosions, make sure you're
; not making any variables global that are used inside these functions! Doh!

; -----------------------------------------------------------------------------
; **** IMPORTANT: doesn't work for MidHandle'd images! ****
; -----------------------------------------------------------------------------

; Only small images should use split sizes like 1 x 1 or 2 x 2! The larger the
; image, the larger the split size you should use (note that large images
; split into 1 x 1 or 2 x 2 can take AGES to pre-render!)...

; -----------------------------------------------------------------------------
; Usage:
; -----------------------------------------------------------------------------

; Startup: Set your display mode, then call InitExplosions ().

; Startup: Optionally pre-render explosions via RenderExplosion ().

; During game: Call StartRenderedExplosion () or StartRealtimeExplosion ()
; to explode an image.

; During game: Call UpdateExplosions somewhere before Flip ().

; -----------------------------------------------------------------------------
; Types used by explosion system...
; -----------------------------------------------------------------------------

; Particles actually drawn and updated on-screen...

Type ExplosionParticle
	Field prerendered
	Field sprite						; Particle image
	Field x#							; x-position on screen
	Field y#							; x-position on screen
	Field xs#							; x speed of particle
	Field ys#							; y speed of particle
End Type

; Pre-rendered explosion -- one created per image, via RenderExplosion ()...

Type RenderedExplosion
	Field rep.RenderedExplosionParticle
End Type

; Pre-rendered particles (not shown on-screen), created by RenderExplosion ()...

Type RenderedExplosionParticle
	Field rendered.RenderedExplosion	; Handle to rendered explosion
	Field sprite						; Particle image
	Field x#							; x-position on screen
	Field y#							; x-position on screen
	Field xs#							; x speed of particle
	Field ys#							; y speed of particle
End Type

; -----------------------------------------------------------------------------
; Globals used by explosion system...
; -----------------------------------------------------------------------------

Global EXPLODE_GW						; Display width
Global EXPLODE_GH						; Display height

Global EXPLODE_Num						; Number of particles currently on screen

Global EXPLODE_Gravity# = 0.05			; Gravity

Global EXPLODE_MaskR					; Images' mask red value
Global EXPLODE_MaskG					; Images' mask green value
Global EXPLODE_MaskB					; Images' mask blue value

Global EXPLODE_DefaultModifier# = 0.025	; Default modifier used when auto-calc'ing force in Start[*]Explosion functions
Global EXPLODE_DefaultMinRand# = 0.25	; Default minimum range of explosion randomiser
Global EXPLODE_DefaultMaxRand# = 1		; Default minimum range of explosion randomiser

Const EXPLODE_DefaultForce# = 0.05		; Default force used by Start[*]Explosion functions

; -----------------------------------------------------------------------------
; Explosion system functions...
; -----------------------------------------------------------------------------

; SETEXPLOSIONRGB ()						[normally just called internally]
; -----------------------------------------------------------------------------
; Alters the global image mask colours. Called by InitExplosions (), though you
; can call it between calls to RenderExplosion () or StartRealtimeExplosion ()
; if you INSIST on using different RGB masks for different images! :P

; PARAMETERS
; -----------------------------------------------------------------------------
; r, g, b -- transparency mask colours used by your image(s)...

Function SetExplosionRGB (r, g, b)
	EXPLODE_MaskR = r					; Images' mask red value
	EXPLODE_MaskG = g					; Images' mask green value
	EXPLODE_MaskB = b					; Images' mask blue value
End Function

; INITEXPLOSIONS ()
; -----------------------------------------------------------------------------
; Store width and height of display for quick access, and (IMPORTANT) sets the RGB
; transparency mask used by your images (NOTE: I've assumed you'll generally use the
; same mask value for all your images; if you do need to change this for different
; images between calls to RenderExplosion () or StartRealtimeExplosion (), use
; SetExplosionRGB ()). Simplest option is to make all your images use the same mask
; colour!

; PARAMETERS
; -----------------------------------------------------------------------------
; r, g, b -- transparency mask colours used by your image(s)...

Function InitExplosions (r = 0, g = 0, b = 0)
	EXPLODE_GW = ClientWidth (window)		; Width of display	} For use by
	EXPLODE_GH = ClientHeight (window)		; Height of display } UpdateExplosions
	SetExplosionRGB (r, g, b)
	SeedRnd MilliSecs ()
End Function

; RENDEREXPLOSION ()
; -----------------------------------------------------------------------------
; Takes an image and pre-renders the split images required, plus pre-calcs the appropriate
; angles for each image (force is added when StartRenderedExplosion () is called). Returns
; a handle to the pre-rendered explosion of type .RenderedExplosion, which you pass to the
; StartRenderedExplosion () function, like so:

; blah.RenderedExplosion = eRenderExplosion (... )
; 	...
; StartRenderedExplosion (blah, [...])

; THIS SHOULD BE CARRIED OUT BEFORE YOUR MAIN LOOP!

; PARAMETERS
; -----------------------------------------------------------------------------
; image				-- the image to be exploded...
; splitx			-- number of horizontal 'splits'...
; splity			-- number of vertical 'splits'...
; frame				-- image frame number (not really tested!)...

Function RenderExplosion.RenderedExplosion (image, splitx = 8, splity = 8, frame = 0)

;	graphics = Graphicsbuffer ()							; Store current draw graphics (probably back)
	SetBuffer ImageBuffer (image)						; Use image's graphics
	iW = ImageWidth (image)								; Store width of image
	iH = ImageHeight (image)							; Store height of image
	cx# = iW / 2										; Pre-calc half of width
	cy# = iH / 2										; Pre-calc half of height

	re.RenderedExplosion = New RenderedExplosion		; Created a pre-rendered explosion

	; This section 'reads' the image in a grid split by splitx and splity,
	; from top down, like so...
	;
	; ||||| OOOOOOOOOO
	; ||||| OOOOOOOOOO
	; ||||| OOOOOOOOOO
	; ||||V OOOOOOOOOO
	; ||||  OOOOOOOOOO
	; |||| OOOOOOOOOOO
	; |||| OOOOOOOOOOO
	; |||| OOOOOOOOOOO

	While px < iW - 1
		While py < iH - 1
			splitxt = splitx							; Temporary splitx } Because...
			splityt = splity							; Temporary splity } ...
			If (px + splitx) > (iW - 1)
				splitxt = iW - px						; Extreme right pieces may be narrower
			EndIf
			If (py + splity) > (iH - 1)
				splityt = iH - py						; Extreme bottom pieces may be shorter
			EndIf
			
			; Check if this section of grid is empty (avoids creating TONS of blank particles)...

			If ImageRectCollide (image, 0, 0, frame, px, py, splitx, splity)
			
				re\rep.RenderedExplosionParticle = New RenderedExplosionParticle ; Created pre-rendered particle
				re\rep\rendered = re								; Handle to pre-rendered explosion instance (IDs this particle as belonging to this type of explosion)...
				re\rep\sprite = CreateImage (splitxt, splityt)		; Make an image of grid section size

				GrabImage re\rep\sprite, px, py						; Grab this part of grid into it
				MaskImage re\rep\sprite, EXPLODE_MaskR, EXPLODE_MaskG, EXPLODE_MaskB ; Mask it

				re\rep\x = px									; Particle's x/y position is image's x/y ->
				re\rep\y = py									; -> position plus current grid position

				; Um... trial and error to 'correct' (ie. randomise) some weirdness with the
				; middle row of particles (stayed in a straight line in some cases). Works :)
				
				xdiff# = cx - px
				ydiff# = cy - py
				ydiff2# = py - cy
				
				If xdiff = 0 Then xdiff = Rnd (8)
				If ydiff = 0 Then ydiff = Rnd (8)
				If ydiff2 = 0 Then ydiff2 = -ydiff
				
				ang# = ATan2 (ydiff, xdiff)						; Angle of grid section from centre
				sinang# = Sin (ang)								; Used twice, so we'll pre-calc it once :)
				vector# = (ydiff2) / sinang						; Speed vector (?) of particle (Pythagoras translation: hypotenuse = opposite / sine of angle)
				If vector = 0 Then vector = Rnd (8)				; 'Correction' of some more weirdness I don't understand...
				
				re\rep\xs = Cos (ang) * vector * Rnd (EXPLODE_DefaultMinRand, EXPLODE_DefaultMaxRand)	; Calculate x speed of particle from angle, vector and force (cosine of angle * vector gets x component; this is then multiplied by the force, and randomise)...
				re\rep\ys = sinang * vector * Rnd (EXPLODE_DefaultMinRand, EXPLODE_DefaultMaxRand)		; Calculate y speed of particle from angle, vector and force (csine of angle * vector gets y component; this is then multiplied by the force, and randomise)...

			EndIf
			py = py + splity							; Next line down...
		Wend											; Done this column, so...
		px = px + splitx								; ... next line across
		py = 0											; Go to top of grid
	Wend
;	SetGraphics graphics									; Reset to graphics stored at start
	
	Return re											; Return handle to pre-rendered explosion
	
End Function

; STARTRENDEREDEXPLOSION ()
; -----------------------------------------------------------------------------
; Sets off an explosion using a handle returned by RenderExplosion (). Copies each pre-rendered
; particle into a 'real' particle for on-screen display/update by UpdateExplosions (). Also adds
; force to the pre-calculated angles, allowing for lots of variation from the same pre-calc'd
; explosion data...

; PARAMETERS
; -----------------------------------------------------------------------------
; rend				-- handle to a pre-rendered explosion (got from RenderExplosion ())...
; x					-- x position on screen for explosion to start...
; y					-- y position on screen for explosion to start...
; xs				-- x speed (optional) of a moving image...
; ys				-- y speed (optional) of a moving image...
; force				-- force of explosion... use 0 for auto-calculation from xs and ys values. Note that
;					   if these are both zero, the default force (EXPLODE_DefaultForce) will be used!

Function StartRenderedExplosion (rend.RenderedExplosion, x#, y#, xs# = 0, ys# = 0, force# = EXPLODE_DefaultForce)
	If force = 0
		force = Abs ((ys / Sin (ATan (ys / xs))) * EXPLODE_DefaultModifier) ; Auto-calc force from xs and ys...
		If force = 0 Then force = EXPLODE_DefaultForce
	EndIf
	For rep.RenderedExplosionParticle = Each RenderedExplosionParticle
		If rep\rendered = rend
			p.ExplosionParticle = New ExplosionParticle		; Create a new particle
			p\prerendered = True
			p\sprite = rep\sprite
			p\x = x + rep\x									; Particle's x/y position is image's x/y ->
			p\y = y + rep\y									; -> position plus current grid position
			p\xs = (rep\xs * force) + xs
			p\ys = (rep\ys * force) + ys
			EXPLODE_Num = EXPLODE_Num + 1
		EndIf
	Next
End Function

; STARTREALTIMEEXPLOSION ()
; -----------------------------------------------------------------------------
; Starts a realtime (ie. not pre-rendered) explosion. Good for small-ish images if you don't
; want a pre-rendered version taking up your graphics memory. Note that this effectively
; does the same as RenderExplosion (), but the differences are that it is slower for
; large/lots of images, but takes up less memory (since it doesn't have to keep a pre-rendered
; version in graphics memory)...

; PARAMETERS
; -----------------------------------------------------------------------------
; image				-- image to be exploded in realtime...
; x					-- x position on screen for explosion to start...
; y					-- y position on screen for explosion to start...
; xs				-- x speed (optional) of a moving image...
; ys				-- y speed (optional) of a moving image...
; force				-- force of explosion...
; splitx			-- number of horizontal 'splits'...
; splity			-- number of vertical 'splits'...
; frame				-- image frame number (not really tested!)...

Function StartRealtimeExplosion (image, x#, y#, xs# = 0, ys# = 0, force# = EXPLODE_DefaultForce, splitx = 8, splity = 8, frame = 0)
	If force = 0 Then force = Abs ((ys / Sin (ATan (ys / xs))) * EXPLODE_DefaultModifier): If force = 0 Then force = EXPLODE_DefaultForce
;	graphics = Graphicsgraphics ()							; Store current draw graphics (probably back)
	SetBuffer ImageBuffer (image)						; Use image's graphics
	iW = ImageWidth (image)								; Store width of image
	iH = ImageHeight (image)							; Store height of image
	cx# = iW / 2										; Pre-calc half of width
	cy# = iH / 2										; Pre-calc half of height

	; This section 'reads' the image in a grid split by splitx and splity,
	; from top down, like so...
	;
	; ||||| OOOOOOOOOO
	; ||||| OOOOOOOOOO
	; ||||| OOOOOOOOOO
	; ||||V OOOOOOOOOO
	; ||||  OOOOOOOOOO
	; |||| OOOOOOOOOOO
	; |||| OOOOOOOOOOO
	; |||| OOOOOOOOOOO

	While px < iW - 1
		While py < iH - 1
			splitxt = splitx							; Temporary splitx } Because...
			splityt = splity							; Temporary splity } ...
			If (px + splitx) > (iW - 1)
				splitxt = iW - px						; Extreme right pieces may be narrower
			EndIf
			If (py + splity) > (iH - 1)
				splityt = iH - py						; Extreme bottom pieces may be shorter
			EndIf

			; Check if this section of grid is empty (avoids creating TONS of blank particles
			; at little cost time-wise in most cases)...

			If ImageRectCollide (image, 0, 0, frame, px, py, splitx, splity)
				EXPLODE_Num = EXPLODE_Num + 1					; Increase global particle counter
				p.ExplosionParticle = New ExplosionParticle		; Create a new particle
				p\sprite = CreateImage (splitxt, splityt)		; Make an image of grid section size
				GrabImage p\sprite, px, py						; Grab this part of grid into it
				MaskImage p\sprite, EXPLODE_MaskR, EXPLODE_MaskG, EXPLODE_MaskB ; Mask it
				p\x = x + px									; Particle's x/y position is image's x/y ->
				p\y = y + py									; -> position plus current grid position
				ang# = ATan2 (cy - py, cx - px)					; Angle of grid section from centre
				sinang# = Sin (ang)								; Used twice, so we'll pre-calc it once :)
				vector# = (py - cy) / sinang					; Speed vector of particle (Pythagoras translation: hypotenuse = opposite / sine of angle)
				p\xs = xs + (Cos (ang) * vector * force * Rnd (EXPLODE_DefaultMinRand, EXPLODE_DefaultMaxRand))	; Calculate x speed of particle from angle, vector and force (cosine of angle * vector gets x component; this is then multiplied by the force, added to the required x speed, and randomised)...
				p\ys = ys + (sinang * vector * force * Rnd (EXPLODE_DefaultMinRand, EXPLODE_DefaultMaxRand))	; Calculate y speed of particle from angle, vector and force (csine of angle * vector gets y component; this is then multiplied by the force, added to the required y speed, and randomised)...
			EndIf
			py = py + splity							; Next line down...
		Wend											; Done this column, so...
		px = px + splitx								; ... next line across
		py = 0											; Go to top of grid
	Wend
;	SetGraphics graphics									; Reset to graphics stored at start
End Function

; UPDATEEXPLOSIONS ()
; -----------------------------------------------------------------------------
; Updates position of particles, draws them, and deletes them if off-screen. Called 'somewhere
; before Flip'...

; PARAMETERS
; -----------------------------------------------------------------------------
; NONE.

Function UpdateExplosions ()
	For p.ExplosionParticle = Each ExplosionParticle	; Process each particle
		p\x = p\x + p\xs								; Move particle by x speed
		p\ys = p\ys + EXPLODE_Gravity					; Add gravity to particle's y speed
		p\y = p\y + p\ys								; Move particle by y speed
		DrawImage p\sprite, p\x, p\y					; Draw particle
; -----------------------------------------------------------------------------
; You may wish to modify the condition check here for killing particles...
; -----------------------------------------------------------------------------
		If ((((p\x - 0) Xor (p\x - EXPLODE_GW)) And ((p\y - 0) Xor (p\y - EXPLODE_GH))) And $80000000) = 0 ; Fast 'in-box' code possibly by Shagwana or AntonyDB? :)
; -----------------------------------------------------------------------------
			If p\prerendered = 0 Then FreeImage p\sprite; Delete image only if NOT pre-rendered (pre-rendered particles use the image from the RenderedExplosionParticle store, which therefore needs to be kept)...
			Delete p									; Delete particle if off-screen
			EXPLODE_Num = EXPLODE_Num - 1
		EndIf
; -----------------------------------------------------------------------------
	Next
End Function

; COUNTEXPLOSIONPARTICLES ()
; -----------------------------------------------------------------------------
; Returns the number of particles in a pre-rendered explosion...

; PARAMETERS
; -----------------------------------------------------------------------------
; rend				-- handle of a pre-rendered explosion returned by RenderExplosion ()

Function CountRenderedExplosionParticles (rend.RenderedExplosion)
	For rep.RenderedExplosionParticle = Each RenderedExplosionParticle
		If rep\rendered = rend
			count = count + 1
		EndIf
	Next
	Return count
End Function

; FREERENDEREDEXPLOSION ()
; -----------------------------------------------------------------------------
; Frees the images and data associated with a pre-rendered explosion, meaning it
; can no longer be used (although your game wonn't crash if you still try to use
; the explosion -- in fact, this can be used while the explosion is taking place,
; although that's not recommended since it'll look a bit crap most of the time :)

; PARAMETERS
; -----------------------------------------------------------------------------
; rend				-- handle of a pre-rendered explosion returned by RenderExplosion ()
; copydrawing		-- normally particles being drawn when this function is called will just
;					   be deleted. If you set copydrawing to True, it will make a copy of any
;					   pre-rendered particle that's about to be deleted, but this can cause
;					   a 'jerk' if there are still lots on-screen. Try to only call this
;					   function WITHOUT copydrawing when there are no explosions happening!

Function FreeRenderedExplosion (rend.RenderedExplosion, copydrawing = False)
	For rep.RenderedExplosionParticle = Each RenderedExplosionParticle		; Loop through all pre-rendered particles
		If rep\rendered = rend												; One from this pre-rendered explosion?
			For p.ExplosionParticle = Each ExplosionParticle				; Check currently drawing particles...
				If p\sprite = rep\sprite									; Using the one we're about to delete?
					If copydrawing
						p\sprite = CopyImage (rep\sprite)					; Either make a copy of the image...
					Else
						Delete p											; Or just delete the particle (default)...
					EndIf
				EndIf
			Next
			FreeImage rep\sprite											; Free the pre-rendered particle image
			Delete rep														; Delete the particle
		EndIf
	Next
	rend.RenderedExplosion = Null											; Set the explosion handle to NULL so it can't be used...
End Function