BlitzPlus open source release (minus audio).

You will need:

* Visual Studio Community 2017. Install "Desktop development with C++" plus "MFC and ATL support" plus "ASP.NET and web development".

To rebuild:

1) Open blitzplus.sln in msvc2017.

2) Select release config and rebuild entire solution.

3) Select debug config and rebuild blitzplus project only.

4) This will rebuild everything into the _release and _release/bin dirs. Feel free to delete .pdb and .ilk files here.

BlitzPlus is released under the zlib/libpng license.

The zlib/libpng License

Copyright (c) 2013 Blitz Research Ltd

This software is provided 'as-is', without any express or implied warranty. In no event will the authors be held liable for any damages arising from the use of this software.

Permission is granted to anyone to use this software for any purpose, including commercial applications, and to alter it and redistribute it freely, subject to the following restrictions:

1. The origin of this software must not be misrepresented; you must not claim that you wrote the original software. If you use this software in a product, an acknowledgment in the product documentation would be appreciated but is not required.

2. Altered source versions must be plainly marked as such, and must not be misrepresented as being the original software.

3. This notice may not be removed or altered from any source distribution.

