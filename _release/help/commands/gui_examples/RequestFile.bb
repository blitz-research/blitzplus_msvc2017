filename$ = RequestFile("Select a filename to save as","png;*.jpg;*.bmp,*",True,"default.png")

success = SaveImage(myImage, filename$)
If success Then Print "Image Saved"