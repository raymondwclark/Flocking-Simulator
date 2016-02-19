// Created by: Raymond Clark, 2014

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.ArrayList;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Point2D.Float;

import java.awt.geom.AffineTransform;


public class Bird {
	
	//Obstacle obstacle;
	
	Point2D.Float position;
	Point2D.Float velocity;
	Point2D.Float acceleration;
	
	float radius;
	float force;
	float speed;
	
	float separationIntensity;
	float alignmentIntensity;
	float cohesionIntensity;
	
	float separationDistance;
	float alignmentDistance;
	float cohesionDistance;
	
	Polygon poly;
	AffineTransform at;
	float angleOfMovement;
	
	  
	Random rand;
	  
	  int r, g, b;
	  Color birdColor;

	    Bird(float x, float y) {
	    	
	    	//obstacle = new Obstacle(300, 300, 20);
	    	
	    	rand = new Random();
	    	
	    	position = new Point2D.Float(x, y);
	    	acceleration = new Point2D.Float(0, 0);
	    	
	        angleOfMovement = (float)(rand.nextInt((int)(2 * Math.PI)));
	    	
	    	velocity = new Point2D.Float((float)Math.cos(angleOfMovement),(float)Math.sin(angleOfMovement));
	    	
	    	radius = 4f;
	    	speed = 2f;
	    	force = .035f;
	    	
	    	separationIntensity = 1.5f;
	    	alignmentIntensity = 1.0f;
	    	cohesionIntensity = 1.0f;
	    	
	    	separationDistance = 25f;
	    	alignmentDistance = cohesionDistance = 50f;
	    	
	    	r = 0;
	    	g = 255;
	    	b = 0;
	    
	    	birdColor = new Color(r,g,b);
	    	
	  }

	  void simulate(ArrayList<Bird> birds) {
		applyRules(birds);
	    updatePosition();	    

	    if (position.x < -radius) position.x = 900+radius;
	    if (position.y < -radius) position.y = 700+radius;
	    if (position.x > 900+radius) position.x = -radius;
	    if (position.y > 700+radius) position.y = -radius;
	  }
	  
	  void updateRuleIntensity(float sep, float ali, float coh) {
		  this.separationIntensity = sep;
		  this.alignmentIntensity = ali;
		  this.cohesionIntensity = coh;
	  }
	  
	  void updateSpeed(float speed) {
		  this.speed = speed;
	  }
	  
	  void updateSize(float size) {
		  this.radius = size;
	  }
	  
	  void applyRules(ArrayList<Bird> Birds) {
		Point2D.Float separation = separate(Birds); 
		Point2D.Float alignment = alignment(Birds);
		Point2D.Float cohesion = cohesion(Birds);
		//Point2D.Float avoid = avoidObject(Birds);
		   
		separation.x *= separationIntensity;
		separation.y *= separationIntensity;
		   
		alignment.x *= alignmentIntensity;
		alignment.y *= alignmentIntensity;
		   
		cohesion.x *= cohesionIntensity;
		cohesion.y *= cohesionIntensity;
		
		//avoid.x *= 2;
		//avoid.y *= 2;
		    
		acceleration.x += separation.x;
		acceleration.y += separation.y;
		acceleration.x += alignment.x;
		acceleration.y += alignment.y;
		acceleration.x += cohesion.x;
		acceleration.y += cohesion.y;	
		
		//acceleration.x += avoid.x;
		//acceleration.y += avoid.y;
	  }

	  void updatePosition() {
		  velocity.x += acceleration.x;
		  velocity.y += acceleration.y;
		  
		  float vMagnitude = (float)Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y);
		  
		  if(vMagnitude > speed) {
			  if (vMagnitude != 0 && vMagnitude != 1) {
			      velocity.x /= vMagnitude;
			      velocity.y /= vMagnitude;
			    }
			  velocity.x *= speed;
			  velocity.y *= speed;
		  }
		  
		  position.x += velocity.x;
		  position.y += velocity.y;
		  
		  acceleration.x *= 0f;
		  acceleration.y *= 0f;
	  }

	  void drawBird(Graphics2D g2d) {
		g2d.setColor(birdColor);
		//double x1 = position.getX() - (position.x - radius);
		//double y1 = position.getY() - (position.y - radius);
		
		//x1 = x1 * Math.cos(angleOfMovement) - y1 * Math.sin(angleOfMovement);
		//y1 = x1 * Math.sin(angleOfMovement) + y1 * Math.cos(angleOfMovement);
		
		//position.setLocation(x1 + position.x, y1 + position.y);
		
		//int[] xList = {(int)(position.x - radius), (int)(position.x), (int)(position.x + radius)};
		//int[] yList = {(int)position.y, (int)(position.y - radius), (int)position.y};
		//poly = new Polygon (xList, yList, 3); 
        //g2d.fillPolygon (poly); 
		g2d.fillOval((int)position.x, (int)position.y, (int)radius * 2, (int)radius * 2);
		
		//obstacle.drawObstacle(g2d);
	    
	  }
	  
	  void changeColor() {			
			birdColor = new Color(r,g,b);
	  }	 
	  
	  Point2D.Float separate (ArrayList<Bird> birds) {
		   Point2D.Float avoid = new Point2D.Float(0,0);
		   int numOfBirds = 0;
		   for(Bird neighbor: birds) {
		      
		      float dx = position.x - neighbor.position.x;
		      float dy = position.y - neighbor.position.y;
		      float d =  (float) Math.sqrt(dx*dx + dy*dy);  //<--runs faster (keep)
		      
		     // float d = (float)Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));  <--slow af
		      
		      if ((d > 0) && (d < separationDistance)) {
		        Point2D.Float distance = new Point2D.Float(position.x - neighbor.position.x, position.y - neighbor.position.y);
		        
		        float dMag = (float)Math.sqrt(distance.x * distance.x + distance.y * distance.y);
		        if(dMag != 0 && dMag != 1) {
		        	distance.x /= dMag;
		        	distance.y /= dMag;
		        }
		        
		        distance.x /= d;
		        distance.y /= d;
		        
		        avoid.x += distance.x;
		        avoid.y += distance.y;
		        
		        numOfBirds++;            
		      }
		    }
		    if (numOfBirds > 0) {
		    	avoid.x /= (float)numOfBirds;
		    	avoid.y /= (float)numOfBirds;
		    }
		    
		    float aMag = (float)Math.sqrt(avoid.x * avoid.x + avoid.y * avoid.y);
		    if (aMag > 0) {
		      if(aMag != 0 && aMag != 1) {
		    	  avoid.x /= aMag;
		    	  avoid.y /= aMag;
		      }
		      avoid.x *= speed;
		      avoid.y *= speed;
		      
		      avoid.x -= velocity.x;
		      avoid.y -= velocity.y;
		      
		      float aMag2 = (float)Math.sqrt(avoid.x * avoid.x + avoid.y * avoid.y);
		      if(aMag2 > force) {
				  if (aMag2 != 0 && aMag2 != 1) {
					  avoid.x /= aMag2;
					  avoid.y /= aMag2;
				    }
				  avoid.x *= force;
				  avoid.y *= force;
			  }
		      
		    }
		    return avoid;
		  }
	  
	  Point2D.Float alignment (ArrayList<Bird> birds) {
		    Point2D.Float avgBirdDirection = new Point2D.Float(0, 0);
		    int numOfBirds = 0;
		    for (Bird neighbor : birds) {
		    	  float dx = position.x - neighbor.position.x;
			      float dy = position.y - neighbor.position.y;
			      float d =  (float) Math.sqrt(dx*dx + dy*dy);
			      
		      if ((d > 0) && (d < alignmentDistance)) {
		    	  avgBirdDirection.x += neighbor.velocity.x;
		    	  avgBirdDirection.y += neighbor.velocity.y;
		    	  
		    	  numOfBirds++;
		      }
		    }
		    if (numOfBirds > 0) {
		    	avgBirdDirection.x /= (float)numOfBirds;
		    	avgBirdDirection.y /= (float)numOfBirds;
		      
		      float aMag = (float)Math.sqrt(avgBirdDirection.x * avgBirdDirection.x + avgBirdDirection.y * avgBirdDirection.y);
		      if(aMag != 0 && aMag != 1) {
		    	  avgBirdDirection.x /= aMag;
		    	  avgBirdDirection.y /= aMag;
		      }
		      
		      avgBirdDirection.x *= speed;
		      avgBirdDirection.y *= speed;
		      
		      Point2D.Float alignDirection = new Point2D.Float(avgBirdDirection.x - velocity.x, avgBirdDirection.y - velocity.y);
		      
		      float aMag2 = (float)Math.sqrt(alignDirection.x * alignDirection.x + alignDirection.y * alignDirection.y);
		      
		      if(aMag2 > force) {
				  if (aMag2 != 0 && aMag2 != 1) {
					  alignDirection.x /= aMag2;
					  alignDirection.y /= aMag2;
				    }
				  alignDirection.x *= force;
				  alignDirection.y *= force;
			  }
		      return alignDirection;
		    } 
		    else {
		      return new Point2D.Float(0, 0);
		    }
		  }
	  
	  Point2D.Float cohesion (ArrayList<Bird> birds) {
		    Point2D.Float centerGravity = new Point2D.Float(0, 0);
		    int numOfBirds = 0;
		    for (Bird neighbor : birds) {
		    	
		    	float dx = position.x - neighbor.position.x;
			    float dy = position.y - neighbor.position.y;
			    float d =  (float) Math.sqrt(dx*dx + dy*dy);
			    
		      if ((d > 0) && (d < cohesionDistance)) {
		    	  centerGravity.x += neighbor.position.x;
		    	  centerGravity.y += neighbor.position.y;
		    	  numOfBirds++;
		      }
		    }
		    if (numOfBirds > 0) {
		      centerGravity.x /= numOfBirds;
		      centerGravity.y /= numOfBirds;
    
		      Point2D.Float cohere;  
			  Point2D.Float pointToNeighbor = new Point2D.Float(centerGravity.x - position.x, centerGravity.y - position.y);  
			    float d = (float)Math.sqrt(pointToNeighbor.x * pointToNeighbor.x + pointToNeighbor.y * pointToNeighbor.y);
			    if (d > 0) {
			    	float dMag = (float)(Math.sqrt(pointToNeighbor.x * pointToNeighbor.x + pointToNeighbor.y * pointToNeighbor.y));
			    	if(dMag != 0 && dMag != 1) {
			    		pointToNeighbor.x /= dMag;
			    		pointToNeighbor.y /= dMag;
			    	}		
			    	pointToNeighbor.x *= speed;
			    	pointToNeighbor.y *= speed;
			    	  
			      cohere = new Point2D.Float(pointToNeighbor.x - velocity.x, pointToNeighbor.y - velocity.y);
			      
			      float sMagnitude = (float)Math.sqrt(cohere.x * cohere.x + cohere.y * cohere.y);
			      
				  if(sMagnitude > force) {
					  if (sMagnitude != 0 && sMagnitude != 1) {
						  cohere.x /= sMagnitude;
						  cohere.y /= sMagnitude;
					    }
					  cohere.x *= force;
					  cohere.y *= force;
				  }
			    } 
			    
			    else {
			    	cohere = new Point2D.Float(0,0);
			    }
			    return cohere;
		    } 
		    else {
		      return new Point2D.Float(0, 0);
		    }
		  }	 
}



