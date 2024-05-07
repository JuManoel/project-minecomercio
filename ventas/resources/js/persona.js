export default class Persona {
    static table
    static modal
    static currentOption
    static form
    static tipos
    static nombreClase
    static colunas
    constructor() {
      throw new Error('No utilice el constructor. Use el .init()')
    }
  
    static async init() {
      try {
  
        // intentar cargar el formulario de edición de productos
        Persona.form = await Helpers.loadPage(`./resources/html/${this.nombreClase}.html`)
  
        // intentar cargar los datos de los productos
        const response = await Helpers.fetchData(`${urlAPI}/${this.nombreClase}`)
        if (response.message !== 'ok') {
          throw new Error(response.message)
        }
        // agregar al <main> de index.html la capa que contendrá la tabla
        document.querySelector('main').innerHTML = `
          <div class="p-2 w-full">
              <div id="table-container" class="m-2"></div>
          </div>`
        // ver en https://tabulator.info/docs/6.2/columnsdefinition cómo se definen las propiedades de las columnas
        // ver en https://tabulator.info/docs/6.2/format los diferentes valores de la propiedad formater de las columnas
        Persona.table = new Tabulator('#table-container', {
          height: tableHeight, // establecer la altura de la tabla, esto habilita el DOM virtual y mejora drásticamente la velocidad de procesamiento
          data: response.data, // asignar los datos a la tabla
          layout: 'fitColumns', // ajustar columnas al ancho disponible
          
          columns: this.colunas,
          // mostrar al final de la tabla un botón para agregar registros
          footerElement: `<div class='container-fluid d-flex justify-content-end p-0'>${addRowButton}</div>`,
        })
  
        // agregar un gestor de eventos al botón 'add-row' para mostrar el formulario en donde se ingresarán productos
        Persona.table.on('tableBuilt', () => document.querySelector('#add-row').addEventListener('click', this.addRow))
      } catch (e) {
        Toast.show({ title: 'Ventas', message: e.message, mode: 'danger', error: e })
      }
      return this
    }
  
    static editRowClick = async (e, cell) => {//cell es la celda que lo llamo
        Persona.currentOption = 'edit'
        Persona.modal = new Modal({
        classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
        title: `<h5>Actualización de ${this.nombreClase}</h5>`,
        content:Persona.form,
        buttons: [
          { caption: editButton, classes: 'btn btn-primary me-2' , action: ()=> Persona.edit(cell) },
          { caption: cancelButton, classes: 'btn btn-secondary',action: ()=> Persona.modal.close() },
        ],
        doSomething: idModal => Persona.toComplete(idModal, cell.getRow().getData()),
      })
    
      try {
        const option = await Persona.modal.show()
        if (option === 'Cancelar' || option === '✖') {
            Persona.modal.close()
        } else if (option === 'Actualizar') {
            Persona.edit(cell)
        }
      } catch (e) {
        Toast({ content: `Problemas al modificar el ${ this.nombreClase}`, mode: 'danger', error: e })
    }
    }
  
    static edit(cell) {
      Toast.show({ title: this.nombreClase, message: `Falta edicion la adición de ${ this.nombreClase}`, mode: 'warning' })
    }
  
    static deleteRowClick = async (e, cell) => {
        Persona.currentOption = 'delete'
        Persona.modal = new Modal({
        classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
        title: `<h5>Eliminación de ${this.nombreClase}</h5>`,
        content: `
          <span class="text-back dark:text-gray-300">
             Confirme la eliminación del ${this.nombreClase}:<br>
             ${cell.getRow().getData().id} - ${cell.getRow().getData().descripcion} - 
             Valor venta $${cell.getRow().getData().valorVenta}<br>
          </span>`, 
        buttons: [
          { caption: deleteButton, classes: 'btn btn-primary me-2', action:()=> Persona.delete(cell) },
          { caption: cancelButton, classes:'btn btn-secondary',action: ()=> Persona.modal.close() },
        ],
        // no requiere doSomething
      })
    
      try {
        const option = await Persona.modal.show()
        if (option === 'Cancelar' || option === '✖') {
            Persona.modal.close()
        } else if (option === 'Eliminar') {
            Persona.delete(cell)
        }
      } catch (e) {
        Toast({ message: `Problemas al deletar el ${ this.nombreClase}`, mode: 'danger', error: e })
    }
    }
    static delete(cell) {
      Toast.show({ title:  this.nombreClase, message: `Falta implementar la eliminacion de ${ this.nombreClase}`, mode: 'warning' })
    }
  
    static getFormData(){
      const id= document.querySelector(`#${Persona.modal.id} #id`).value
      const nombre= document.querySelector(`#${Persona.modal.id} #nombre`).value
      const telefono= document.querySelector(`#${Persona.modal.id} #telefono`).value
      const correo= document.querySelector(`#${Persona.modal.id} #correo`).value
      const password= document.querySelector(`#${Persona.modal.id} #contrasena`).value
      return { id, nombre, nombre, telefono, correo, password}
    }

    static toComplete(idModal, rowData) {
    
      if (Persona.currentOption === 'edit') {
        // mostrar los datos de la fila actual en el formulario html
        document.querySelector(`#${idModal} #id`).value = rowData.id
        document.querySelector(`#${idModal} #nombre`).value = rowData.nombre
        document.querySelector(`#${idModal} #correo`).value = rowData.correo
        document.querySelector(`#${idModal} #telefono`).value = rowData.telefono
        document.querySelector(`#${idModal} #contrasena`).value = rowData.contrasena 
      }
    }

  }
  