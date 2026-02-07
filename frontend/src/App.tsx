import React from "react";
import { Container, ThemeProvider } from "@mui/material";
import { theme } from "./theme";
import { MainLayout } from "./layouts";
import PatientManagement from "./features/patient/PatientManagement.tsx";
import { Header } from "./shared/components/Header.tsx";
import BillingSection from "./features/billing/BillingSection.tsx";
import { AppProvider } from "./providers";

export const App = (): React.ReactNode => {
  return (
      <ThemeProvider theme={theme}>
          <AppProvider>
              <MainLayout>
                  <Header />
                  <Container maxWidth="lg">
                      <BillingSection />
                  </Container>
                  <PatientManagement />
              </MainLayout>
          </AppProvider>
      </ThemeProvider>
  )
}