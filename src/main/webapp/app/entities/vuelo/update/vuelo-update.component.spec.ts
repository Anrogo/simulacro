import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VueloService } from '../service/vuelo.service';
import { IVuelo, Vuelo } from '../vuelo.model';
import { IAeropuerto } from 'app/entities/aeropuerto/aeropuerto.model';
import { AeropuertoService } from 'app/entities/aeropuerto/service/aeropuerto.service';

import { VueloUpdateComponent } from './vuelo-update.component';

describe('Vuelo Management Update Component', () => {
  let comp: VueloUpdateComponent;
  let fixture: ComponentFixture<VueloUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vueloService: VueloService;
  let aeropuertoService: AeropuertoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VueloUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(VueloUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VueloUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vueloService = TestBed.inject(VueloService);
    aeropuertoService = TestBed.inject(AeropuertoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Aeropuerto query and add missing value', () => {
      const vuelo: IVuelo = { id: 456 };
      const origen: IAeropuerto = { id: 34038 };
      vuelo.origen = origen;
      const destino: IAeropuerto = { id: 69703 };
      vuelo.destino = destino;

      const aeropuertoCollection: IAeropuerto[] = [{ id: 33008 }];
      jest.spyOn(aeropuertoService, 'query').mockReturnValue(of(new HttpResponse({ body: aeropuertoCollection })));
      const additionalAeropuertos = [origen, destino];
      const expectedCollection: IAeropuerto[] = [...additionalAeropuertos, ...aeropuertoCollection];
      jest.spyOn(aeropuertoService, 'addAeropuertoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vuelo });
      comp.ngOnInit();

      expect(aeropuertoService.query).toHaveBeenCalled();
      expect(aeropuertoService.addAeropuertoToCollectionIfMissing).toHaveBeenCalledWith(aeropuertoCollection, ...additionalAeropuertos);
      expect(comp.aeropuertosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vuelo: IVuelo = { id: 456 };
      const origen: IAeropuerto = { id: 81311 };
      vuelo.origen = origen;
      const destino: IAeropuerto = { id: 25090 };
      vuelo.destino = destino;

      activatedRoute.data = of({ vuelo });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(vuelo));
      expect(comp.aeropuertosSharedCollection).toContain(origen);
      expect(comp.aeropuertosSharedCollection).toContain(destino);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vuelo>>();
      const vuelo = { id: 123 };
      jest.spyOn(vueloService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vuelo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vuelo }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(vueloService.update).toHaveBeenCalledWith(vuelo);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vuelo>>();
      const vuelo = new Vuelo();
      jest.spyOn(vueloService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vuelo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vuelo }));
      saveSubject.complete();

      // THEN
      expect(vueloService.create).toHaveBeenCalledWith(vuelo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vuelo>>();
      const vuelo = { id: 123 };
      jest.spyOn(vueloService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vuelo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vueloService.update).toHaveBeenCalledWith(vuelo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAeropuertoById', () => {
      it('Should return tracked Aeropuerto primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAeropuertoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
