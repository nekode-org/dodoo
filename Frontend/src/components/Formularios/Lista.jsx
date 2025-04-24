import React, { useEffect, useState } from 'react';
import styles from '../Estilos/Lista.module.css';

const Datos = () => {
    const [tickets, setTickets] = useState([]); // Estado para almacenar los tickets

    useEffect(() => {
        // Leer la cookie
        const responseData = leerCookie('data');
        if (responseData) {
            const data = JSON.parse(responseData);
            console.log('Datos recibidos:', data);

            // Guardar los tickets en el estado
            setTickets(data.tickets || []); // Asegúrate de que sea un array

            // Eliminar la cookie después de procesarla
            document.cookie = 'data=; path=/; max-age=0';
        }
    }, []);

    const leerCookie = (nombre) => {
        const cookies = document.cookie.split('; ');
        const cookie = cookies.find((c) => c.startsWith(`${nombre}=`));
        return cookie ? decodeURIComponent(cookie.split('=')[1]) : null;
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
                            <div className={styles.columna}>{ticket.ticketCode}</div>
                            <div className={styles.columna}>{ticket.fechaConsumo}</div>
                            <div className={styles.columna}>{ticket.monto}</div>
                        </React.Fragment>
                    ))}
                </div>
            </div>
            <div className={styles.Datos}>
            <h3>Validación de datos</h3>
            <div className={styles.Form}>
                <div className={styles.div}>
                    <label htmlFor="email">Correo electronico</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        placeholder="Correo electronico"
                        required
                        // value={formData.email}
                        // onChange={handleInputChange}
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
                        // value={formData.pais}
                        // onChange={handleInputChange}
                        required
                    />
                </div>
                <div className={styles.div}>
                    <label htmlFor="rfc">RFC</label>
                    <input
                        type="text"
                        id="rfc"
                        name="rfc"
                        // value={formData.pais}
                        // onChange={handleInputChange}
                        required
                    />
                </div>
                <div className={styles.div}>
                    <label htmlFor="razon">Nombre o razon social</label>
                    <input
                        type="text"
                        id="razon"
                        name="razon"
                        // value={formData.pais}
                        // onChange={handleInputChange}
                        required
                    />
                </div>
                <div className={styles.div}>
                    <label htmlFor="regimen">Regimen fiscal</label>
                    <select name="regimen" id="regimen">
                        <option value="">Seleccione un regimen fiscal</option>
                        <option value="opcion2">Opción 2</option>
                        <option value="opcion3">Opción 3</option>
                        <option value="opcion4">Opción 4</option>
                        <option value="opcion5">Opción 5</option>
                        <option value="opcion1">Opción 1</option>
                        <option value="opcion2">Opción 2</option>
                        <option value="opcion3">Opción 3</option>
                        <option value="opcion4">Opción 4</option>
                        <option value="opcion5">Opción 5</option>
                    </select>
                </div>
                <div className={styles.div}>
                    <label htmlFor="cfdi">Uso de CFID</label>
                    <select name="cfid" id="cfid">
                        <option value="">Seleccione un regimen fiscal</option>
                        <option value="opcion2">Opción 2</option>
                        <option value="opcion3">Opción 3</option>
                        <option value="opcion4">Opción 4</option>
                        <option value="opcion5">Opción 5</option>
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
                        // value={formData.pais}
                        // onChange={handleInputChange}
                        required
                    />
                </div>
                <div className={styles.div}>
                    <label htmlFor="ext">Número exterior</label>
                    <input
                        type="text"
                        id="ext"
                        name="ext"
                        // value={formData.pais}
                        // onChange={handleInputChange}
                        required
                    />
                </div>
                <div className={styles.div}>
                    <label htmlFor="intt">Número interior</label>
                    <input
                        type="text"
                        id="intt"
                        name="intt"
                        // value={formData.pais}
                        // onChange={handleInputChange}
                        required
                    />
                </div>
                <div className={styles.div}>
                    <label htmlFor="enti">Entidad Federativa</label>
                    <input
                        type="text"
                        id="enti"
                        name="enti"
                        // value={formData.pais}
                        // onChange={handleInputChange}
                        required
                    />
                </div>
                <div className={styles.div}>
                    <label htmlFor="municipio">Municipio</label>
                    <input
                        type="text"
                        id="municipio"
                        name="municipio"
                        // value={formData.pais}
                        // onChange={handleInputChange}
                        required
                    />
                </div>
                <div className={styles.div}>
                    <label htmlFor="col">Colonia</label>
                    <input
                        type="text"
                        id="col"
                        name="col"
                        // value={formData.pais}
                        // onChange={handleInputChange}
                        required
                    />
                </div>
                <div className={styles.div}>
                    <label htmlFor="cp">Código postal</label>
                    <input
                        type="text"
                        id="cp"
                        name="cp"
                        // value={formData.pais}
                        // onChange={handleInputChange}
                        required
                    />
                </div>
            </div>
            </div>
        </div>
    );
};

export default Datos;