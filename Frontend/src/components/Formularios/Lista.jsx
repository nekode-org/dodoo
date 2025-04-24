import React, { useEffect, useState } from 'react';
import styles from '../Estilos/Lista.module.css';

// Este componente recupera los datos de los tickets (folio, fecha y monto) y los 
// del cliente (si es que existen), en caso contrario se le pide al usuario que
// ingrese los datos necesarios para la facturación.

const Datos = () => {
    const [tickets, setTickets] = useState([]); // Estado para almacenar los tickets
    const [formValues, setFormValues] = useState({}); // Estado para manejar los valores del formulario

    useEffect(() => {
        // Leer la cookie
        const responseData = leerCookie('data');
        if (responseData) {
            const data = JSON.parse(responseData);
            console.log('Datos recibidos:', data);

            // Guardar los tickets y los valores iniciales del formulario
            setTickets(data.ticketResults || []); // Asegúrate de que sea un array
            setFormValues(data.infoApiSat || {}); // Asegúrate de que sea un objeto

            // Eliminar la cookie después de procesarla
            document.cookie = 'data=; path=/; max-age=0';
        }
    }, []);

    const leerCookie = (nombre) => {
        const cookies = document.cookie.split('; ');
        const cookie = cookies.find((c) => c.startsWith(`${nombre}=`));
        return cookie ? decodeURIComponent(cookie.split('=')[1]) : null;
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormValues({
            ...formValues,
            [name]: value,
        });
    };

    const enviarFormulario = async () => {
        const dataToSend = {
            ticketResults: tickets,
            infoApiSat: formValues,
        };

        console.log('Datos a enviar:', JSON.stringify(dataToSend));

        try {
            const response = await fetch('/providers/confirmInfo', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(dataToSend),
            });

            if (!response.ok) {
                throw new Error('Error en la respuesta del servidor');
            }

            const responseData = await response.json();
            console.log('Respuesta del servidor:', responseData);
            alert('Datos enviados con éxito');
            window.location.href = '/index.html';
        } catch (error) {
            console.error('Error al enviar los datos:', error);
            alert('Hubo un error al enviar los datos');
        }
    };

    return (
        <div className={styles.Main}>
            <div className={styles.contenedor}>
                <h3>Tickets a facturar</h3>
                <div className={styles.contenido}>
                    {/* Encabezados */}
                    <div className={styles.columna}>No. de Ticket</div>
                    <div className={styles.columna}>Fecha de consumo</div>
                    <div className={styles.columna}>Monto</div>

                    {/* Iterar sobre los tickets */}
                    {tickets.map((ticket, index) => (
                        <React.Fragment key={index}>
                            <div className={styles.columna}>{ticket.folio}</div>
                            <div className={styles.columna}>{ticket.fecha}</div>
                            <div className={styles.columna}>{ticket.monto}</div>
                        </React.Fragment>
                    ))}
                </div>
            </div>
            <div className={styles.Datos}>
                <h3>Validación de datos</h3>
                <div className={styles.Form}>
                    <div className={styles.div}>
                        <label htmlFor="email">Correo electrónico</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            placeholder="Correo electrónico"
                            value={formValues.email || ''}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className={styles.div1}>

                        <ol className={styles.lista}>
                            <li>Revisa que todos los campos estén completos y correctos.</li>
                            <li>Si encuentras algún dato incorrecto, corrígelo antes de continuar.</li>
                            <li>Cuando todo esté listo, haz clic en el botón “Aceptar” para generar tu factura.</li>
                        </ol>
                    </div>
                    <div className={styles.div}>
                        <label htmlFor="pais">País de origen</label>
                        <input
                            type="text"
                            id="pais"
                            name="pais"
                            value={formValues.pais || ''}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className={styles.div}>
                        <label htmlFor="rfc">RFC</label>
                        <input
                            type="text"
                            id="rfc"
                            name="rfc"
                            value={formValues.rfc || ''}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className={styles.div}>
                        <label htmlFor="razonSocial">Nombre o razón social</label>
                        <input
                            type="text"
                            id="razonSocial"
                            name="razonSocial"
                            value={formValues.razonSocial || ''}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className={styles.div}>
                        <label htmlFor="regimen">Régimen fiscal</label>
                        <select
                            name="regimen"
                            id="regimen"
                            value={formValues.regimen || ''}
                            onChange={handleInputChange}
                        >
                            <option value="">Seleccione un régimen fiscal</option>
                            <option value="opcion1">Opción 1</option>
                            <option value="opcion2">Opción 2</option>
                            <option value="opcion3">Opción 3</option>
                            <option value="opcion4">Opción 4</option>
                            <option value="opcion5">Opción 5</option>
                        </select>
                    </div>
                    <div className={styles.div}>
                        <label htmlFor="cfdi">Uso de CFDI</label>
                        <select
                            name="cfdi"
                            id="cfdi"
                            value={formValues.cfdi || ''}
                            onChange={handleInputChange}
                        >
                            <option value="">Seleccione un uso de CFDI</option>
                            <option value="opcion1">Opción 1</option>
                            <option value="opcion2">Opción 2</option>
                            <option value="opcion3">Opción 3</option>
                            <option value="opcion4">Opción 4</option>
                            <option value="opcion5">Opción 5</option>
                        </select>
                    </div>
                    <div className={styles.div}>
                        <label htmlFor="calle">Calle</label>
                        <input
                            type="text"
                            id="calle"
                            name="calle"
                            value={formValues.calle || ''}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className={styles.div}>
                        <label htmlFor="noExt">Número exterior</label>
                        <input
                            type="text"
                            id="noExt"
                            name="noExt"
                            value={formValues.noExt || ''}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className={styles.div}>
                        <label htmlFor="noInt">Número interior</label>
                        <input
                            type="text"
                            id="noInt"
                            name="noInt"
                            value={formValues.noInt || ''}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className={styles.div}>
                        <label htmlFor="entidadFederativa">Entidad Federativa</label>
                        <input
                            type="text"
                            id="entidadFederativa"
                            name="entidadFederativa"
                            value={formValues.entidadFederativa || ''}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className={styles.div}>
                        <label htmlFor="municipio">Municipio</label>
                        <input
                            type="text"
                            id="municipio"
                            name="municipio"
                            value={formValues.municipio || ''}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className={styles.div}>
                        <label htmlFor="colonia">Colonia</label>
                        <input
                            type="text"
                            id="colonia"
                            name="colonia"
                            value={formValues.colonia || ''}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className={styles.div}>
                        <label htmlFor="codigoPostal">Código postal</label>
                        <input
                            type="text"
                            id="codigoPostal"
                            name="codigoPostal"
                            value={formValues.codigoPostal || ''}
                            onChange={handleInputChange}
                        />
                    </div>
                </div>
                <button className={styles.boton} onClick={enviarFormulario}>
                    Enviar
                </button>
            </div>
        </div>
    );
};

export default Datos;