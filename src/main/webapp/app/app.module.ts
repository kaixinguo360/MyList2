import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
import { ServiceWorkerModule } from '@angular/service-worker';

import { environment } from '../environments/environment';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { MaterialModules } from './material-modules.module';
import { LoginComponent } from './login/login.component';
import { ListComponent } from './list/list.component';
import { ListDetailComponent } from './list-detail/list-detail.component';
import { ListEditComponent } from './list-edit/list-edit.component';
import { ItemDetailComponent, ItemDetailDialogComponent } from './item-detail/item-detail.component';
import { ItemEditComponent, ItemEditDialogComponent } from './item-edit/item-edit.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ListComponent,
    ListDetailComponent,
    ListEditComponent,
    ItemDetailDialogComponent,
    ItemDetailComponent,
    ItemEditDialogComponent,
    ItemEditComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    MaterialModules,
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: environment.production })
  ],
  providers: [],
  bootstrap: [ AppComponent ],
  entryComponents: [
    ItemDetailDialogComponent,
    ItemEditDialogComponent
  ]
})
export class AppModule { }
