import React from 'react';

const Folio = ({ folio, onInputChange }) => {
  return (
    <div className="Elemento">
      <input
        type="text"
        name="ticketCode" // Cambiado de "folio" a "ticketCode"
        placeholder="Folio de ticket"
        value={folio.ticketCode} // Asegurarse de usar "ticketCode"
        onChange={(e) => onInputChange(folio.id, 'ticketCode', e.target.value)} // Actualizar "ticketCode"
      />
      <select
        name="providerCode" // Cambiado de "empresa" a "providerCode"
        value={folio.providerCode || ''} // Asegurarse de usar "providerCode"
        onChange={(e) => onInputChange(folio.id, 'providerCode', parseInt(e.target.value, 10))} // Actualizar "providerCode"
      >
        <option value="" disabled>Selecciona una empresa</option>
        <option value="1">Empresa 1</option>
        <option value="2">Empresa 2</option>
        <option value="3">Empresa 3</option>
      </select>
    </div>
  );
};

export default Folio;