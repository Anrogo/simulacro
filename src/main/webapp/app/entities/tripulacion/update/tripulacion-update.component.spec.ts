import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TripulacionService } from '../service/tripulacion.service';
import { ITripulacion, Tripulacion } from '../tripulacion.model';

import { TripulacionUpdateComponent } from './tripulacion-update.component';

describe('Tripulacion Management Update Component', () => {
  let comp: TripulacionUpdateComponent;
  let fixture: ComponentFixture<TripulacionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tripulacionService: TripulacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TripulacionUpdateComponent],
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
      .overrideTemplate(TripulacionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TripulacionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tripulacionService = TestBed.inject(TripulacionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tripulacion: ITripulacion = { id: 456 };

      activatedRoute.data = of({ tripulacion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tripulacion));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Tripulacion>>();
      const tripulacion = { id: 123 };
      jest.spyOn(tripulacionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tripulacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tripulacion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tripulacionService.update).toHaveBeenCalledWith(tripulacion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Tripulacion>>();
      const tripulacion = new Tripulacion();
      jest.spyOn(tripulacionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tripulacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tripulacion }));
      saveSubject.complete();

      // THEN
      expect(tripulacionService.create).toHaveBeenCalledWith(tripulacion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Tripulacion>>();
      const tripulacion = { id: 123 };
      jest.spyOn(tripulacionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tripulacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tripulacionService.update).toHaveBeenCalledWith(tripulacion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
