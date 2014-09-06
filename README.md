Gears-Engine
============

Basic java game engine. Probably version like 0.2 or something like that.

This game engine was developed alongside M.U.L.E. as a simple 2D framework in Java that would give us a simple 60fps double buffered display canvas to work with, and a few utility classes for moving around sprites. There is support for animated sprites, a fairly sturdy input system for keyboard and mouse, and a few geometry classes. It also has a stacked state manager for game states, as well as riduamentary tilemap support. There is currently no collision detection performed by the engine, though it's set up in a way that adding it would be very simple, given all sprites have a base rect that defines a simple axis aligned bounding box.
