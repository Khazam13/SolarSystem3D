package application;

import java.util.LinkedList;

import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.util.Duration;
/**
 * Singleton class which provides a well known access point for all AnimationTimer and Transition
 * 
 * There are only 5 public methods available
 * - getInstance
 * - addTransition
 * - addAnimationTimer
 * - start
 * - stop
 * 
 * Provide for more functionality to slow down /speed up the rotation / orbital transits
 * 
 * As some planet - satellite systems consist of tide-locked objects orbital and rotation scaling should be synchronized
 *  
 * 
 * @author RKastner
 *
 */
public class AnimationHandler {
	private final static AnimationHandler instance = new AnimationHandler();

	LinkedList<PathTransition> transitions = new LinkedList<>();
	/*
	 * List of timers for the transitions, updated when the animation is stopped and read when
	 * animation continues
	 */
	LinkedList<Duration> runningTimer = new LinkedList<>();
	LinkedList<AnimationTimer> animationTimers = new LinkedList<>();

	private boolean running = false;

	/*
	 * only constructor private to ensure singleton
	 */
	private AnimationHandler() {
	}

	public static AnimationHandler getInstance() {
		return instance;
	}
	
	/**
	 * Add a PathTransition object to the list of managed transitions.
	 * transitions are used for orbital movements of astronomical objects
	 * Ensure non null and non duplicate
	 */
	public void addTransition(PathTransition t) {
		if (t == null) return;
		for(PathTransition transition : transitions) {
			if (t.equals(transition)) return;
		}
		transitions.add(t);
		runningTimer.add(t.getCurrentTime());
	}
	/**
	 * Add a AnimationTimer object to the list of managed animation timers.
	 * Animation timers are used for rotation of astronomical objects
	 * Ensure non null and non duplicate
	 */
	public void addAnimationTimer(AnimationTimer a) {
		if (a == null) return;
		for(AnimationTimer animationTimer : animationTimers) {
			if (a.equals(animationTimer)) return;
		}
		animationTimers.add(a);
	}
//	public void start(Duration from) {
//		for(PathTransition t : transitions) {
//			t.playFrom(from);
//		}
//		for(AnimationTimer a : animationTimers) {
//			a.start();
//		}
//		running = true;
//	}
	public void start() {
		for(PathTransition t : transitions) {
			t.play();
		}
		for(AnimationTimer a : animationTimers) {
			a.start();
		}
		running = true;
	}
	public void unpause() {
		int i = 0;
		for(PathTransition t : transitions) {
			Duration from = runningTimer.get(i++);
			System.out.println("(unpause) " + from);
			t.playFrom(from);
		}
		for(AnimationTimer a : animationTimers) {
			a.start();
		}
		running = true;
	}
	public void pause() {
		// we only need to save the state of the transitions, the animation timers used for rotation
		// don't have a meaningful state
		int i = 0;
		for(PathTransition t : transitions) {
			Duration from = t.getCurrentTime();
			runningTimer.set(i++, from);
			t.stop();
		}
		for(AnimationTimer a : animationTimers) {
			a.stop();
		}
		running = false;
	}
	public void stop() {
		for(PathTransition t : transitions) {
			t.stop();
		}
		for(AnimationTimer a : animationTimers) {
			a.stop();
		}
		running = false;
	}
	public void toggle() {
		if (running) {
			pause();
		} else {
			unpause();
		}
	}
}
