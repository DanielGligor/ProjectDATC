
import {Component, OnInit, ElementRef, ViewChild} from '@angular/core';
import {registerElement} from "nativescript-angular/element-registry";
import { MapView, Position } from 'nativescript-google-maps-sdk';
import * as GoogleMapsUtils from "nativescript-google-maps-utils";
// Important - must register MapView plugin in order to use in Angular templates
registerElement('MapView', () => MapView);

@Component({
    selector: "Home",
    moduleId: module.id,
    templateUrl: "./home.component.html"
})
export class HomeComponent implements OnInit {

    public position: any=[];
    public mapView: MapView;
    latitude =  -33.86;
    longitude = 151.20;
    zoom = 8;
    minZoom = 0;
    maxZoom = 22;
    bearing = 0;
    tilt = 0;
    padding = [40, 40, 40, 40];

    //Map events
   public onMapReady(event) {
        console.log("Map Ready");
        this.mapView = event.object;
       // console.log(GoogleMapsUtils);
        this.position[0] = Position.positionFromLatLng(45.745622488138366, 21.22827367832417);
       GoogleMapsUtils.setupHeatmap(this.mapView, this.position);
    };
    constructor() {
        // Use the component constructor to inject providers.
    }

    ngOnInit(): void {
        // Init your component properties here.
    }
}
